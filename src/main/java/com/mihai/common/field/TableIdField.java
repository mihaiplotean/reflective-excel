package com.mihai.common.field;

import java.lang.reflect.Field;

public class TableIdField implements AnnotatedField {

    private final Field field;
    private final String tableId;

    public TableIdField(Field field, String tableId) {
        this.field = field;
        this.tableId = tableId;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AnnotatedFieldType getType() {
        return AnnotatedFieldType.TABLE_ID;
    }

    public String getTableId() {
        return tableId;
    }
}
