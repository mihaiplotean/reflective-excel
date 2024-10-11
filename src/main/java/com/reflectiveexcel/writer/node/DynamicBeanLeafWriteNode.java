package com.reflectiveexcel.writer.node;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class DynamicBeanLeafWriteNode implements ChildBeanWriteNode {

    private final Object name;
    private final ColumnSizePreferences sizePreferences;

    public DynamicBeanLeafWriteNode(Object name, ColumnSizePreferences sizePreferences) {
        this.name = name;
        this.sizePreferences = sizePreferences;
    }

    @Override
    public Object getName() {
        return name;
    }

    @Override
    public Field getField() {
        return null;
    }

    @Override
    public int getLength() {
        return 1;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public List<ChildBeanWriteNode> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public List<TypedValue> getLeafValues(Object target) {
        return Collections.emptyList();
    }

    @Override
    public boolean isLeafValue() {
        return true;
    }

    @Override
    public ColumnSizePreferences getColumnSize() {
        return sizePreferences;
    }
}
