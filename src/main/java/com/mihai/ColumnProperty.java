package com.mihai;

import java.lang.reflect.Field;

public class ColumnProperty {

    private final String columnName;
    private final Field field;


    public ColumnProperty(String columnName, Field field) {
        this.columnName = columnName;
        this.field = field;
    }

    public String getColumnName() {
        return columnName;
    }

    public Field getField() {
        return field;
    }
}
