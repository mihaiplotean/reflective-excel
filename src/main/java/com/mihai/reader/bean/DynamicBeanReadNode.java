package com.mihai.reader.bean;

import com.mihai.common.field.AnnotatedField;
import com.mihai.common.field.AnnotatedFieldType;
import com.mihai.common.field.DynamicColumnField;

import java.util.List;

public class DynamicBeanReadNode implements ChildBeanReadNode {

    private final DynamicColumnField field;

    public DynamicBeanReadNode(DynamicColumnField field) {
        this.field = field;
    }

    @Override
    public AnnotatedField getAnnotatedField() {
        return field;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<ChildBeanReadNode> getChildren() {
        return List.of();
    }

    @Override
    public List<ChildBeanReadNode> getLeaves() {
        return List.of(this);
    }

    @Override
    public AnnotatedFieldType getAnnotatedFieldType() {
        return AnnotatedFieldType.DYNAMIC;
    }
}
