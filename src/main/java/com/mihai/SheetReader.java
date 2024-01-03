package com.mihai;

import com.mihai.annotation.*;
import com.mihai.deserializer.DeserializationContext;
import com.mihai.detector.DynamicColumnDetector;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public class SheetReader {

    private final Sheet sheet;
    private final ExcelReadingSettings settings;
    private final DeserializationContext deserializationContext;
    private final CellValueFormatter cellValueFormatter;

    private Map<Integer, ColumnProperty> columnIndexToPropertyMap = Collections.emptyMap();

    public SheetReader(Sheet sheet, DeserializationContext deserializationContext, ExcelReadingSettings settings) {
        this.sheet = sheet;
        this.deserializationContext = deserializationContext;
        this.settings = settings;
        this.cellValueFormatter = new CellValueFormatter(sheet.getWorkbook());
    }

    public <T> List<T> readRows(Class<T> clazz) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<T> rows = new ArrayList<>();

        for (Row row : sheet) {
            if (row.getRowNum() < settings.getHeaderStartRow()) {
                continue;
            }
            if (row.getRowNum() == settings.getHeaderStartRow()) {
                columnIndexToPropertyMap = getColumnIndexToClassFieldMap(row, clazz);
                continue;
            }
            RowCells rowCells = getRowCellDetails(row);
            if (settings.isEndRow(rowCells)) {
                break;
            }
            rows.add(createRow(clazz, rowCells));
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
            if (cell.getColumnIndex() >= settings.getHeaderStartColumn()) {
                headers.add(new ColumnIndex(cell.getColumnIndex(), cellValueFormatter.toString(cell)));
            }
        }

        for (Cell cell : headerRow) {
            if (cell.getColumnIndex() < settings.getHeaderStartColumn()) {
                continue;
            }
            String headerName = cellValueFormatter.toString(cell);
            Field field = columnNameToFieldMap.get(headerName);  // todo: make it case insensitive
            ExcelCell cellWrapper = cellWrapper(cell, headerName);
            if (field != null) {
                columnIndexToClassFieldMap.put(cell.getColumnIndex(), ColumnProperty.fixedColumn(cellWrapper, field));
            } else {
                for (Map.Entry<Class<? extends DynamicColumnDetector>, Field> entry : dynamicColumnDetectorToFieldMap.entrySet()) {
                    Class<? extends DynamicColumnDetector> dynamicColumnDetectorClass = entry.getKey();
                    DynamicColumnDetector detector = dynamicColumnDetectorClass.getConstructor().newInstance();
                    boolean dynamicColumn = detector.isDynamicColumn(
                            new MaybeDynamicColumn(headers,
                                    new ColumnIndex(cell.getColumnIndex(), cellValueFormatter.toString(cell))));
                    if (dynamicColumn) {
                        columnIndexToClassFieldMap.put(cell.getColumnIndex(), ColumnProperty.dynamicColumn(cellWrapper, entry.getValue()));
                        break;
                    }
                }
            }
        }
        return columnIndexToClassFieldMap;
    }

    private RowCells getRowCellDetails(Row row) {
        List<ExcelCell> excelCellDetails = new ArrayList<>();
        for (Cell cell : row) {
            if (cell.getColumnIndex() < settings.getHeaderStartColumn()) {
                continue;
            }
            int columnIndex = cell.getColumnIndex();
            ColumnProperty columnProperty = columnIndexToPropertyMap.get(columnIndex);
            if (columnProperty != null) {
                excelCellDetails.add(cellWrapper(cell, columnProperty.getColumnName()));
            }
        }
        return new RowCells(excelCellDetails);
    }

    private ExcelCell cellWrapper(Cell cell, String columnName) {
        return new ExcelCell.CellDetailsBuilder()
                .cell(cell)
                .cellValue(cellValueFormatter.toString(cell))
                .columnName(columnName)
                .build();
    }

    private <T> T createRow(Class<T> clazz, RowCells rowCells)
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

    public <T> T read(Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T properties = clazz.getConstructor().newInstance();

        Map<String, Field> cellReferenceToFieldMap = FieldUtils.getFieldsListWithAnnotation(clazz, ExcelCellValue.class).stream()
                .collect(Collectors.toMap(field -> field.getAnnotation(ExcelCellValue.class).cellReference(), field -> field, (a, b) -> a));
        for (Map.Entry<String, Field> entry : cellReferenceToFieldMap.entrySet()) {
            CellReference cellReference = new CellReference(entry.getKey());
            int row = cellReference.getRow();
            int column = cellReference.getCol();
            Cell cell = getCell(row, column);  // todo: can be null
            ExcelCell cellWrapper = cellWrapper(cell, null);

            Field field = entry.getValue();
            Object fieldValue = deserializationContext.deserialize(field.getType(), cellWrapper);
            FieldUtils.writeField(field, properties, fieldValue, true);
        }

        Map<ExcelProperty, Field> propertyToFieldMap = FieldUtils.getFieldsListWithAnnotation(clazz, ExcelProperty.class).stream()
                .collect(Collectors.toMap(field -> field.getAnnotation(ExcelProperty.class), field -> field, (a, b) -> a));
        for (Map.Entry<ExcelProperty, Field> entry : propertyToFieldMap.entrySet()) {
            ExcelProperty property = entry.getKey();
            CellReference cellReference = new CellReference(property.cellReference());
            int row = cellReference.getRow();
            int column = cellReference.getCol();
            Cell cell = getCell(row, column);    // todo: can be null
            if (!cell.getStringCellValue().equalsIgnoreCase(property.name())) {
                throw new IllegalStateException("Property name does not match the one defined");
            }

            Cell valueCell;    // todo: can be null
            ExcelPropertyValueLocation location = property.valueLocation();
            if (location == ExcelPropertyValueLocation.ON_THE_LEFT) {
                valueCell = getCell(row, column + 1);

            } else if (location == ExcelPropertyValueLocation.BELLOW) {
                valueCell = getCell(row + 1, column);
            } else {
                throw new IllegalStateException("unknown location");
            }
            ExcelCell cellWrapper = cellWrapper(valueCell, null);

            Field field = entry.getValue();
            Object fieldValue = deserializationContext.deserialize(field.getType(), cellWrapper);
            FieldUtils.writeField(field, properties, fieldValue, true);  // todo: support primitives
        }

        List<Field> excelRowsFields = FieldUtils.getFieldsListWithAnnotation(clazz, ExcelRows.class);
        for (Field excelRowsField : excelRowsFields) {
            if (excelRowsField.getType().isAssignableFrom(ArrayList.class)) {
                ParameterizedType genericType = (ParameterizedType) excelRowsField.getGenericType();
                Class<?> argumentType = (Class<?>) genericType.getActualTypeArguments()[0];  // todo: unsafe cast?
                List<?> rows = readRows(argumentType);
                FieldUtils.writeField(excelRowsField, properties, rows, true);
            }
        }

        return properties;
    }

    private Cell getCell(int rowIndex, int columnIndex) {
        return Optional.ofNullable(sheet.getRow(rowIndex))
                .map(row -> row.getCell(columnIndex))
                .orElse(null);
    }
}
