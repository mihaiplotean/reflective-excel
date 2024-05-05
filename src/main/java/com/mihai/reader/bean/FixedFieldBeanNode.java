package com.mihai.reader.bean;

import com.mihai.common.field.AnnotatedField;
import com.mihai.common.field.AnnotatedFieldType;
import com.mihai.common.field.FixedColumnField;

import java.util.List;

public class FixedFieldBeanNode implements ChildBeanNode {

    private final FixedColumnField field;

    public FixedFieldBeanNode(FixedColumnField field) {
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
    public List<ChildBeanNode> getChildren() {
        return List.of();
    }

    @Override
    public List<ChildBeanNode> getLeaves() {
        return List.of(this);
    }

    @Override
    public AnnotatedFieldType getAnnotatedFieldType() {
        return AnnotatedFieldType.FIXED;
    }
}
