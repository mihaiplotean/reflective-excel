package com.reflectiveexcel.writer.node;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import com.reflectiveexcel.core.utils.ReflectionUtilities;
import com.reflectiveexcel.writer.annotation.ColumnSize;

public class FixedBeanWriteNode implements ChildBeanWriteNode {

    private final Field field;
    private final String name;
    private final ColumnSizePreferences sizePreferences;

    public FixedBeanWriteNode(Field field, String name) {
        this.field = field;
        this.name = name;
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
        return 1;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public List<ChildBeanWriteNode> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public List<TypedValue> getLeafValues(Object target) {
        return Collections.singletonList(new TypedValue(field.getType(), ReflectionUtilities.readField(field, target)));
    }

    @Override
    public boolean isLeafValue() {
        return true;
    }

    @Override
    public ColumnSizePreferences getColumnSize() {
        return sizePreferences;
    }
}
