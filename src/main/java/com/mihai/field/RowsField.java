package com.mihai.field;

import com.mihai.RowReader;
import com.mihai.field.value.AnnotatedFieldValue;
import com.mihai.field.value.RowsFieldValue;

import java.lang.reflect.Field;

public class RowsField implements AnnotatedField {

    private final RowReader rowReader;
    private final Field field;

    public RowsField(RowReader rowReader, Field field) {
        this.rowReader = rowReader;
        this.field = field;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AnnotatedFieldValue newFieldValue() {
        return new RowsFieldValue(rowReader, field);
    }
}
