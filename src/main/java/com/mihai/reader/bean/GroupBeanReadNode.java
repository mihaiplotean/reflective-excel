package com.mihai.reader.bean;

import com.mihai.common.field.AnnotatedField;
import com.mihai.common.field.AnnotatedFieldType;
import com.mihai.common.field.GroupedColumnsField;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GroupBeanReadNode implements ChildBeanReadNode {

    private final GroupedColumnsField field;
    private final List<ChildBeanReadNode> children;

    public GroupBeanReadNode(GroupedColumnsField field) {
        this.field = field;
        this.children = new ChildBeanReadNodeCreator(field.getFieldType()).getChildFields();
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
    public List<ChildBeanReadNode> getChildren() {
        return children;
    }

    @Override
    public List<ChildBeanReadNode> getLeaves() {
        return children.stream()
                .map(ChildBeanReadNode::getLeaves)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public AnnotatedFieldType getAnnotatedFieldType() {
        return AnnotatedFieldType.GROUPED;
    }
}
