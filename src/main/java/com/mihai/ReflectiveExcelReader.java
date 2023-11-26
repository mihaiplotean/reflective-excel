package com.mihai;

import com.mihai.deserializer.CellDeserializer;
import com.mihai.deserializer.DefaultDeserializationContext;
import com.mihai.deserializer.DeserializationContext;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReflectiveExcelReader {

    private final File file;

    private DeserializationContext deserializationContext;

    private Map<Integer, ColumnProperty> columnIndexToPropertyMap = Collections.emptyMap();

    public ReflectiveExcelReader(File file) {
        this.file = file;
        this.deserializationContext = new DefaultDeserializationContext();
    }

    public void setDeserializationContext(DeserializationContext deserializationContext) {
        this.deserializationContext = deserializationContext;
    }

    public <T> void registerDeserializer(Class<T> clazz, CellDeserializer<T> deserializer) {
        deserializationContext.registerDeserializer(clazz, deserializer);
    }

    public <T> List<T> readRows(Class<T> clazz) {
        ExcelRow rowDetails = clazz.getAnnotation(ExcelRow.class);
        List<T> rows = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file)) {
            if (workbook.getNumberOfSheets() == 0) {
                return Collections.emptyList();
            }
            Sheet sheet = getSheet(workbook, rowDetails);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    columnIndexToPropertyMap = getColumnIndexToClassFieldMap(row, clazz);
                    continue;
                }
                rows.add(createRow(clazz, getRowCellDetails(row)));
            }
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }

        return List.copyOf(rows);
    }

    private Map<Integer, ColumnProperty> getColumnIndexToClassFieldMap(Row row, Class<?> clazz) {
        Map<String, Field> columnNameToFieldMap = FieldUtils.getFieldsListWithAnnotation(clazz, ExcelColumn.class).stream()
                .collect(Collectors.toMap(field -> field.getAnnotation(ExcelColumn.class).name(), field -> field, (a, b) -> a));
        Map<Integer, ColumnProperty> columnIndexToClassFieldMap = new HashMap<>();
        for (Cell cell : row) {
            String headerName = getCellValueAsString(cell);
            Field field = columnNameToFieldMap.get(headerName);
            if (field != null) {
                columnIndexToClassFieldMap.put(cell.getColumnIndex(), new ColumnProperty(headerName, field));
            }
        }
        return columnIndexToClassFieldMap;
    }

    private Sheet getSheet(Workbook workbook, ExcelRow row) {
        String sheetName = row.sheetName();
        if (sheetName.isBlank()) {
            return workbook.getSheetAt(0);
        }
        return workbook.getSheet(sheetName);
    }

    private List<CellDetails> getRowCellDetails(Row row) {
        List<CellDetails> cellDetails = new ArrayList<>();
        for (Cell cell : row) {
            int columnIndex = cell.getColumnIndex();
            ColumnProperty columnProperty = columnIndexToPropertyMap.get(columnIndex);
            if (columnProperty != null) {
                cellDetails.add(new CellDetails.CellDetailsBuilder()
                        .columnName(columnProperty.getColumnName())
                        .cellValue(getCellValueAsString(cell))
                        .cellType(cell.getCellType())
                        .rowIndex(cell.getRowIndex())
                        .columnIndex(columnIndex)
                        .build());
            }
        }
        return cellDetails;
    }

    private static String getCellValueAsString(Cell cell) {
        DataFormatter dataFormatter = new DataFormatter();
        return dataFormatter.formatCellValue(cell);
    }

    private <T> T createRow(Class<T> clazz, List<CellDetails> rowDetails)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T row = clazz.getConstructor().newInstance();
        for (CellDetails cellDetails : rowDetails) {
            Field field = columnIndexToPropertyMap.get(cellDetails.getColumnIndex()).getField();
            Object fieldValue = deserializationContext.deserialize(field.getType(), cellDetails);
            FieldUtils.writeField(field, row, fieldValue, true);
        }
        return row;
    }
}
