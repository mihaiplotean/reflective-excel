package com.mihai.field;

import com.mihai.detector.DynamicColumnDetector;
import com.mihai.field.value.AnnotatedFieldValue;
import com.mihai.field.value.DynamicColumnFieldValue;

import java.lang.reflect.Field;

public class DynamicColumnField implements AnnotatedField {

    private final DynamicColumnDetector columnDetector;
    private final Field field;

    public DynamicColumnField(DynamicColumnDetector columnDetector, Field field) {
        this.columnDetector = columnDetector;
        this.field = field;
    }

    public DynamicColumnDetector getColumnDetector() {
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
