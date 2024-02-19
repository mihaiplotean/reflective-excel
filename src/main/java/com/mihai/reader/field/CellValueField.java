package com.mihai.reader.field;

import com.mihai.reader.field.value.AnnotatedFieldValue;
import com.mihai.reader.field.value.CellValueFieldValue;

import java.lang.reflect.Field;

public class CellValueField implements AnnotatedField {

    private final String cellValueReference;
    private final Field field;

    public CellValueField(String cellValueReference, Field field) {
        this.cellValueReference = cellValueReference;
        this.field = field;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AnnotatedFieldValue newFieldValue() {
        return new CellValueFieldValue(cellValueReference, field);
    }
}
