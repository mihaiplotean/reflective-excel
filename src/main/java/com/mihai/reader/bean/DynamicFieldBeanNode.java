package com.mihai.reader.bean;

import com.mihai.reader.field.AnnotatedFieldType;
import com.mihai.reader.field.DynamicColumnField;

import java.lang.reflect.Field;
import java.util.List;

public class DynamicFieldBeanNode implements ChildBeanNode {

    private final DynamicColumnField field;

    public DynamicFieldBeanNode(DynamicColumnField field) {
        this.field = field;
    }

    @Override
    public Field getField() {
        return field.getField();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public List<? extends ChildBeanNode> getChildren() {
        return List.of();
    }

    @Override
    public List<? extends ChildBeanNode> getLeaves() {
        return List.of();
    }

    @Override
    public AnnotatedFieldType getAnnotatedFieldType() {
        return AnnotatedFieldType.DYNAMIC;
    }
}
