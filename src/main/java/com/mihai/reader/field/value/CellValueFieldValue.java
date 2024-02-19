package com.mihai.reader.field.value;

import com.mihai.reader.ReadingContext;
import com.mihai.ReflectionUtilities;

import java.lang.reflect.Field;

public class CellValueFieldValue implements AnnotatedFieldValue {

    private final String cellValueReference;
    private final Field field;

    private Object fieldValue;

    public CellValueFieldValue(String cellValueReference, Field field) {
        this.cellValueReference = cellValueReference;
        this.field = field;
    }

    @Override
    public void writeTo(Object targetObject) {
        ReflectionUtilities.writeField(field, targetObject, fieldValue);
    }

    @Override
    public void readValue(ReadingContext context) {
        fieldValue = context.getCellValue(cellValueReference, field.getType());
    }
}
