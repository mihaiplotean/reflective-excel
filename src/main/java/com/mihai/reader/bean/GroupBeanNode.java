package com.mihai.reader.bean;

import com.mihai.reader.field.AnnotatedFieldType;
import com.mihai.reader.field.GroupedColumnsField;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class GroupBeanNode implements ChildBeanNode {

    private final GroupedColumnsField field;
    private final List<? extends ChildBeanNode> children;

    public GroupBeanNode(GroupedColumnsField field) {
        this.field = field;
        this.children = new ChildBeanNodeCreator(field.getFieldType()).getChildFields();
    }

    @Override
    public Field getField() {
        return field.getField();
    }

    @Override
    public String getName() {
        return field.getGroupName();
    }

    @Override
    public int getLength() {
        return children.stream()
                .mapToInt(ChildBeanNode::getLength)
                .sum();
    }

    @Override
    public int getHeight() {
        return children.stream()
                .mapToInt(ChildBeanNode::getHeight)
                .sum();
    }

    @Override
    public List<? extends ChildBeanNode> getChildren() {
        return children;
    }

    @Override
    public List<? extends ChildBeanNode> getLeaves() {
        return children.stream()
                .map(ChildBeanNode::getLeaves)
                .flatMap(Collection::stream)
                .toList();
    }

    @Override
    public AnnotatedFieldType getAnnotatedFieldType() {
        return AnnotatedFieldType.GROUPED;
    }
}
