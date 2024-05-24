package com.mihai.writer.node;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class DynamicBeanLeafWriteNode implements ChildBeanWriteNode {

    private final Class<?> type;
    private final Object name;

    public DynamicBeanLeafWriteNode(Class<?> type, Object name) {
        this.name = name;
        this.type = type;
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
    public Class<?> getType() {
        return type;
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
}
