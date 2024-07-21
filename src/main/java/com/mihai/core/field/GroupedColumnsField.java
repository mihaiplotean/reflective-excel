package com.mihai.core.field;

import java.lang.reflect.Field;

public class GroupedColumnsField implements AnnotatedField {

    private final String groupName;
    private final Field field;

    public GroupedColumnsField(String groupName, Field field) {
        this.groupName = groupName;
        this.field = field;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AnnotatedFieldType getType() {
        return AnnotatedFieldType.GROUPED;
    }

    public String getGroupName() {
        return groupName;
    }
}
