package com.reflectiveexcel.writer.node;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import com.reflectiveexcel.core.utils.ReflectionUtilities;
import com.reflectiveexcel.writer.annotation.ColumnSize;

public class DynamicBeanWriteNode implements ChildBeanWriteNode {

    private final Field field;
    private final Map<Object, Object> columnToValueMap;
    private final Class<?> valueType;
    private final ColumnSizePreferences sizePreferences;

    public DynamicBeanWriteNode(Field field, Object target) {
        this.field = field;
        if (field.getType() == Map.class) {
            columnToValueMap = (Map<Object, Object>) ReflectionUtilities.readField(field, target);
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            // safe cast bellow, as DynamicBeanWriteNode is created from a DynamicColumnField, which validates this
            valueType = (Class<?>) genericType.getActualTypeArguments()[1];
        } else {
            throw new IllegalStateException("Only Map.class fields can be annotated as dynamic writable fields!");
        }
        ColumnSize sizeAnnotation = field.getAnnotation(ColumnSize.class);
        this.sizePreferences = sizeAnnotation != null
                ? new ColumnSizePreferences(sizeAnnotation.max(), sizeAnnotation.preferred(), sizeAnnotation.min())
                : ColumnSizePreferences.DEFAULT;
    }

    @Override
    public Object getName() {
        return null;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public int getLength() {
        return columnToValueMap.values().size();
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public List<? extends ChildBeanWriteNode> getChildren() {
        return columnToValueMap.keySet().stream()
                .map(column -> new DynamicBeanLeafWriteNode(column, sizePreferences))
                .toList();
    }

    @Override
    public List<TypedValue> getLeafValues(Object target) {
        Map<Object, Object> columnToValueMap = (Map<Object, Object>) ReflectionUtilities.readField(field, target);
        return this.columnToValueMap.keySet().stream()
                .map(key -> new TypedValue(valueType, columnToValueMap.get(key)))
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
