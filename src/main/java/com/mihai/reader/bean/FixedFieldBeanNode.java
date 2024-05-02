package com.mihai.reader.bean;

import com.mihai.reader.field.AnnotatedFieldType;
import com.mihai.reader.field.FixedColumnField;

import java.lang.reflect.Field;
import java.util.List;

public class FixedFieldBeanNode implements ChildBeanNode {

    private final FixedColumnField field;

    public FixedFieldBeanNode(FixedColumnField field) {
        this.field = field;
    }

    @Override
    public Field getField() {
        return field.getField();
    }

    @Override
    public String getName() {
        return field.getColumnName();
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
    public List<? extends ChildBeanNode> getChildren() {
        return List.of();
    }

    @Override
    public List<? extends ChildBeanNode> getLeaves() {
        return List.of(this);
    }

    @Override
    public AnnotatedFieldType getAnnotatedFieldType() {
        return AnnotatedFieldType.FIXED;
    }
}
