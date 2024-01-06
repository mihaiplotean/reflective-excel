package com.mihai.field;

import com.mihai.detector.ColumnDetector;
import com.mihai.field.value.AnnotatedFieldValue;
import com.mihai.field.value.DynamicColumnFieldValue;

import java.lang.reflect.Field;

public class DynamicColumnField implements AnnotatedField {

    private final ColumnDetector columnDetector;
    private final Field field;

    public DynamicColumnField(ColumnDetector columnDetector, Field field) {
        this.columnDetector = columnDetector;
        this.field = field;
    }

    public ColumnDetector getColumnDetector() {
        return columnDetector;
    }

    public Field getField() {
        return field;
    }

    @Override
    public AnnotatedFieldValue newFieldValue() {
        return new DynamicColumnFieldValue(field);
    }
}
