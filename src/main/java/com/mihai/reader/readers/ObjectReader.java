package com.mihai.reader.readers;

import com.mihai.common.field.CellValueField;
import com.mihai.common.field.FieldAnalyzer;
import com.mihai.common.field.KeyValueField;
import com.mihai.common.field.TableIdField;
import com.mihai.common.utils.ReflectionUtilities;
import com.mihai.reader.ExcelReadingSettings;
import com.mihai.reader.ReadableSheetContext;
import com.mihai.reader.exception.BadInputException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

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
        if (tableIdField.getFieldType() != List.class) {
            throw new IllegalStateException("Only List.class can be annotated as row value");
        }
        Field field = tableIdField.getField();
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        Class<?> argumentType = (Class<?>) genericType.getActualTypeArguments()[0];  // todo: unsafe cast?

        List<?> rows = tableReader.readRows(argumentType, tableIdField.getTableId());
        ReflectionUtilities.writeField(tableIdField.getField(), object, rows);
    }
}
