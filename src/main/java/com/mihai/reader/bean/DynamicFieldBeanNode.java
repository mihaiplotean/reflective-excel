package com.mihai.reader.bean;

import com.mihai.common.field.AnnotatedField;
import com.mihai.common.field.AnnotatedFieldType;
import com.mihai.common.field.DynamicColumnField;

import java.util.List;

public class DynamicFieldBeanNode implements ChildBeanNode {

    private final DynamicColumnField field;

    public DynamicFieldBeanNode(DynamicColumnField field) {
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
    public List<ChildBeanNode> getChildren() {
        return List.of();
    }

    @Override
    public List<ChildBeanNode> getLeaves() {
        return List.of(this);
    }

    @Override
    public AnnotatedFieldType getAnnotatedFieldType() {
        return AnnotatedFieldType.DYNAMIC;
    }
}
