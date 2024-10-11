package com.reflectiveexcel.writer.node;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import com.reflectiveexcel.writer.annotation.ColumnSize;

public class GroupBeanWriteNode implements ChildBeanWriteNode {

    private final Field field;
    private final Object target;
    private final String name;
    private final List<ChildBeanWriteNode> children;
    private final ColumnSizePreferences sizePreferences;

    public GroupBeanWriteNode(Field field, Object target, String name) {
        this.field = field;
        this.target = target;
        this.name = name;
        this.children = new ChildBeanWriteNodeCreator(field.getType(), target).getChildFields();
        ColumnSize sizeAnnotation = field.getAnnotation(ColumnSize.class);
        this.sizePreferences = sizeAnnotation != null
                ? new ColumnSizePreferences(sizeAnnotation.max(), sizeAnnotation.preferred(), sizeAnnotation.min())
                : ColumnSizePreferences.DEFAULT;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public int getLength() {
        return children.stream()
                .mapToInt(ChildBeanWriteNode::getLength)
                .sum();
    }

    @Override
    public int getHeight() {
        return 1 + children.stream()
                .mapToInt(ChildBeanWriteNode::getHeight)
                .max()
                .orElse(0);
    }

    @Override
    public List<ChildBeanWriteNode> getChildren() {
        return children;
    }

    @Override
    public List<TypedValue> getLeafValues(Object row) {
        return children.stream()
                .map(child -> child.getLeafValues(target))
                .flatMap(Collection::stream)
                .toList();
    }

    @Override
    public boolean isLeafValue() {
        return false;
    }

    @Override
    public ColumnSizePreferences getColumnSize() {
        return sizePreferences;
    }
}
