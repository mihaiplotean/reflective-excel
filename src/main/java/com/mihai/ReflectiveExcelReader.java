package com.mihai;

import com.mihai.annotation.DynamicColumns;
import com.mihai.annotation.ExcelColumn;
import com.mihai.deserializer.CellDeserializer;
import com.mihai.deserializer.DefaultDeserializationContext;
import com.mihai.deserializer.DeserializationContext;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectiveExcelReader {

    private final File file;
    private final ExcelReadingSettings settings;

    private DeserializationContext deserializationContext;

    private Map<Integer, ColumnProperty> columnIndexToPropertyMap = Collections.emptyMap();

    public ReflectiveExcelReader(File file) {
        this(file, ExcelReadingSettings.DEFAULT);
    }

    public ReflectiveExcelReader(File file, ExcelReadingSettings settings) {
        this.file = file;
        this.settings = settings;
        this.deserializationContext = new DefaultDeserializationContext();
    }

    public void setDeserializationContext(DeserializationContext deserializationContext) {
        this.deserializationContext = deserializationContext;
    }

    public <T> void registerDeserializer(Class<T> clazz, CellDeserializer<T> deserializer) {
        deserializationContext.registerDeserializer(clazz, deserializer);
    }

    public <T> List<T> readRows(Class<T> clazz) {
        List<T> rows = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file)) {
            if (workbook.getNumberOfSheets() == 0) {
                return Collections.emptyList();
            }
            Sheet sheet = getSheet(workbook);
            for (Row row : sheet) {
                if (row.getRowNum() < settings.getHeaderStartRow()) {
                    continue;
                }
                if (row.getRowNum() == settings.getHeaderStartRow()) {
                    columnIndexToPropertyMap = getColumnIndexToClassFieldMap(row, clazz);
                    continue;
                }
                rows.add(createRow(clazz, getRowCellDetails(row)));
            }
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | IOException e) {
            throw new RuntimeException(e);
        }

        return List.copyOf(rows);
    }

    private Map<Integer, ColumnProperty> getColumnIndexToClassFieldMap(Row headerRow, Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Map<String, Field> columnNameToFieldMap = FieldUtils.getFieldsListWithAnnotation(clazz, ExcelColumn.class).stream()
                .collect(Collectors.toMap(field -> field.getAnnotation(ExcelColumn.class).name(), field -> field, (a, b) -> a));
        Map<Class<? extends DynamicColumnDetector>, Field> dynamicColumnDetectorToFieldMap = FieldUtils.getFieldsListWithAnnotation(clazz, DynamicColumns.class).stream()
                .collect(Collectors.toMap(field -> field.getAnnotation(DynamicColumns.class).detector(), field -> field, (a, b) -> a));
        Map<Integer, ColumnProperty> columnIndexToClassFieldMap = new HashMap<>();

        List<ColumnIndex> headers = new ArrayList<>();
        for (Cell cell : headerRow) {
            if (settings.getHeaderStartColumn() >= cell.getColumnIndex()) {
                headers.add(new ColumnIndex(cell.getColumnIndex(), getCellValueAsString(cell)));
            }
        }

        for (Cell cell : headerRow) {
            if (settings.getHeaderStartColumn() < cell.getColumnIndex()) {
                continue;
            }
            String headerName = getCellValueAsString(cell);
            Field field = columnNameToFieldMap.get(headerName);
            ExcelCell cellWrapper = cellWrapper(cell, headerName);
            if (field != null) {
                columnIndexToClassFieldMap.put(cell.getColumnIndex(), ColumnProperty.fixedColumn(cellWrapper, field));
            } else {
                for (Map.Entry<Class<? extends DynamicColumnDetector>, Field> entry : dynamicColumnDetectorToFieldMap.entrySet()) {
                    Class<? extends DynamicColumnDetector> dynamicColumnDetectorClass = entry.getKey();
                    DynamicColumnDetector detector = dynamicColumnDetectorClass.getConstructor().newInstance();
                    boolean dynamicColumn = detector.isDynamicColumn(
                            new MaybeDynamicColumn(headers,
                                    new ColumnIndex(cell.getColumnIndex(), getCellValueAsString(cell))));
                    if (dynamicColumn) {
                        columnIndexToClassFieldMap.put(cell.getColumnIndex(), ColumnProperty.dynamicColumn(cellWrapper, entry.getValue()));
                        break;
                    }
                }
            }
        }
        return columnIndexToClassFieldMap;
    }

    private Sheet getSheet(Workbook workbook) {
        String sheetName = settings.getSheetName();
        if (sheetName == null) {
            return workbook.getSheetAt(settings.getSheetIndex());
        }
        return workbook.getSheet(sheetName);
    }

    private List<ExcelCell> getRowCellDetails(Row row) {
        List<ExcelCell> excelCellDetails = new ArrayList<>();
        for (Cell cell : row) {
            if(cell.getColumnIndex() < settings.getHeaderStartColumn()) {
                continue;
            }
            int columnIndex = cell.getColumnIndex();
            ColumnProperty columnProperty = columnIndexToPropertyMap.get(columnIndex);
            if (columnProperty != null) {
                excelCellDetails.add(cellWrapper(cell, columnProperty.getColumnName()));
            }
        }
        return excelCellDetails;
    }

    private static ExcelCell cellWrapper(Cell cell, String columnName) {
        return new ExcelCell.CellDetailsBuilder()
                .cell(cell)
                .cellValue(getCellValueAsString(cell))
                .columnName(columnName)
                .build();
    }

    private static String getCellValueAsString(Cell cell) {
        DataFormatter dataFormatter = new DataFormatter();
        return dataFormatter.formatCellValue(cell);
    }

    private <T> T createRow(Class<T> clazz, List<ExcelCell> rowCells)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T row = clazz.getConstructor().newInstance();

        Map<Field, Object> initializedObjectPerField = new HashMap<>();

        for (ExcelCell excelCell : rowCells) {
            ColumnProperty columnProperty = columnIndexToPropertyMap.get(excelCell.getColumnIndex());
            Field field = columnProperty.getField();


            if (columnProperty.isDynamic()) {
                if (field.getType().isAssignableFrom(ArrayList.class)) {
                    List<Object> values = (List<Object>) initializedObjectPerField.computeIfAbsent(field, key -> new ArrayList<>());
                    ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                    Class<?> argumentType = (Class<?>) genericType.getActualTypeArguments()[0];  // todo: unsafe cast?
                    Object listValue = deserializationContext.deserialize(argumentType, excelCell);
                    values.add(listValue);
                }
                if (field.getType().isAssignableFrom(LinkedHashMap.class)) {
                    Map<Object, Object> values = (Map<Object, Object>) initializedObjectPerField.computeIfAbsent(field, key -> new LinkedHashMap<>());
                    ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                    Class<?> keyArgumentType = (Class<?>) genericType.getActualTypeArguments()[0];  // todo: unsafe cast?
                    Class<?> valueArgumentType = (Class<?>) genericType.getActualTypeArguments()[1];  // todo: unsafe cast?
                    Object mapKey = deserializationContext.deserialize(keyArgumentType, columnProperty.getCell());
                    Object mapValue = deserializationContext.deserialize(valueArgumentType, excelCell);
                    values.put(mapKey, mapValue);
                }
            } else {
                Object fieldValue = deserializationContext.deserialize(field.getType(), excelCell);
                initializedObjectPerField.put(field, fieldValue);
            }

        }

        for (Map.Entry<Field, Object> entry : initializedObjectPerField.entrySet()) {
            Field field = entry.getKey();
            Object value = entry.getValue();
            FieldUtils.writeField(field, row, value, true);
        }

        return row;
    }
}
