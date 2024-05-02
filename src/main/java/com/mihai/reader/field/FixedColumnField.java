package com.mihai.reader.field;

import java.lang.reflect.Field;

public class FixedColumnField implements AnnotatedField {

    private final String columnName;
    private final Field field;

    public FixedColumnField(String columnName, Field field) {
        this.columnName = columnName;
        this.field = field;
    }

    public String getColumnName() {
        return columnName;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AnnotatedFieldType getType() {
        return AnnotatedFieldType.FIXED;
    }
}
