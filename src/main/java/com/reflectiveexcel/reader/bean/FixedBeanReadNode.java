package com.reflectiveexcel.reader.bean;

import java.util.List;

import com.reflectiveexcel.core.field.AnnotatedField;
import com.reflectiveexcel.core.field.AnnotatedFieldType;
import com.reflectiveexcel.core.field.FixedColumnField;

public class FixedBeanReadNode implements ChildBeanReadNode {

    private final FixedColumnField field;

    public FixedBeanReadNode(FixedColumnField field) {
        this.field = field;
    }

    @Override
    public AnnotatedField getAnnotatedField() {
        return field;
    }

    @Override
    public String getName() {
        return field.getColumnName();
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
        return AnnotatedFieldType.FIXED;
    }
}
