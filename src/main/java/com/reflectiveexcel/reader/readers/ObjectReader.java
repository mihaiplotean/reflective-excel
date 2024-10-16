package com.reflectiveexcel.reader.readers;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.reflectiveexcel.core.field.CellValueField;
import com.reflectiveexcel.core.field.FieldAnalyzer;
import com.reflectiveexcel.core.field.KeyValueField;
import com.reflectiveexcel.core.field.TableIdField;
import com.reflectiveexcel.core.utils.ReflectionUtilities;
import com.reflectiveexcel.reader.ExcelReadingSettings;
import com.reflectiveexcel.reader.ReadableSheetContext;
import com.reflectiveexcel.reader.exception.BadInputException;

public class ObjectReader {

    private final ReadableSheetContext sheetContext;
    private final ExcelReadingSettings settings;

    public ObjectReader(ReadableSheetContext sheetContext, ExcelReadingSettings settings) {
        this.sheetContext = sheetContext;
        this.settings = settings;
    }

    public <T> T read(Class<T> clazz) {
        T object = ReflectionUtilities.newObject(clazz);

        FieldAnalyzer fieldAnalyzer = new FieldAnalyzer(clazz);

        for (CellValueField valueField : fieldAnalyzer.getExcelCellValueFields()) {
            readCellValues(valueField, object);
        }

        for (KeyValueField propertyField : fieldAnalyzer.getExcelPropertyFields()) {
            readPropertyValuePairs(propertyField, object);
        }

        TableReader tableReader = new TableReader(sheetContext, settings);
        for (TableIdField tableIdField : fieldAnalyzer.getTableIdFields()) {
            readTable(tableReader, tableIdField, object);
        }

        return object;
    }

    private <T> void readCellValues(CellValueField field, T object) {
        sheetContext.setCurrentRow(field.getRow());
        sheetContext.setCurrentColumn(field.getColumn());

        Object value = sheetContext.getCellValue(field.getCellValueReference(), field.getFieldType());
        ReflectionUtilities.writeField(field.getField(), object, value);

        sheetContext.resetCellPointer();
    }

    private <T> void readPropertyValuePairs(KeyValueField field, T object) {
        sheetContext.setCurrentRow(field.getPropertyRow());
        sheetContext.setCurrentColumn(field.getPropertyColumn());

        String actualPropertyName = sheetContext.getCellValue(field.getNameReference());
        if (!field.getPropertyName().equalsIgnoreCase(actualPropertyName)) {
            throw new BadInputException(String.format(
                    "Property name \"%s\" at cell %s does not match the one expected %s".formatted(
                            actualPropertyName, field.getNameReference(), field.getPropertyName()
                    ))
            );
        }

        sheetContext.setCurrentRow(field.getValueRow());
        sheetContext.setCurrentColumn(field.getValueColumn());

        Object value = sheetContext.getCellValue(field.getValueReference(), field.getFieldType());
        ReflectionUtilities.writeField(field.getField(), object, value);
    }

    private <T> void readTable(TableReader tableReader, TableIdField tableIdField, T object) {
        Field field = tableIdField.getField();
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        Class<?> argumentType = (Class<?>) genericType.getActualTypeArguments()[0];

        List<?> rows = tableReader.readRows(argumentType, tableIdField.getTableId());
        ReflectionUtilities.writeField(tableIdField.getField(), object, rows);
    }
}
