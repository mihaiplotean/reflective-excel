package com.mihai.reader.bean;

import com.mihai.reader.field.AnnotatedField;
import com.mihai.reader.field.AnnotatedFieldType;
import com.mihai.reader.field.GroupedColumnsField;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GroupBeanNode implements ChildBeanNode {

    private final GroupedColumnsField field;
    private final List<ChildBeanNode> children;

    public GroupBeanNode(GroupedColumnsField field) {
        this.field = field;
        this.children = new ChildBeanNodeCreator(field.getFieldType()).getChildFields();
    }

    @Override
    public AnnotatedField getAnnotatedField() {
        return field;
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
    public List<ChildBeanNode> getChildren() {
        return children;
    }

    @Override
    public List<ChildBeanNode> getLeaves() {
        return children.stream()
                .map(ChildBeanNode::getLeaves)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public AnnotatedFieldType getAnnotatedFieldType() {
        return AnnotatedFieldType.GROUPED;
    }
}
