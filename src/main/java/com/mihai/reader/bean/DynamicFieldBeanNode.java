package com.mihai.reader.bean;

import com.mihai.reader.field.AnnotatedField;
import com.mihai.reader.field.AnnotatedFieldType;
import com.mihai.reader.field.DynamicColumnField;

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
    public int getLength() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
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
