package com.mihai.writer.node;

import com.mihai.ReflectionUtilities;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

public class DynamicFieldNode implements AnnotatedFieldNodeInterface {

    private final Field field;
    private final Map<Object, Object> columnToValueMap;
    private final Class<?> valueType;

    public DynamicFieldNode(Field field, Object target) {
        this.field = field;
        if (field.getType() == Map.class) {
            columnToValueMap = (Map<Object, Object>) ReflectionUtilities.readField(field, target);
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            valueType = (Class<?>) genericType.getActualTypeArguments()[1];  // todo: unsafe cast?
        }
        else {
            throw new IllegalArgumentException("Field type not allowed");
        }
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
    public Class<?> getType() {
        return field.getType();
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
    public List<? extends AnnotatedFieldNodeInterface> getChildren() {
        return columnToValueMap.keySet().stream()
                .map(value -> new DynamicFieldLeafNode(valueType, value))
                .toList();
    }

    @Override
    public List<TypedValue> getLeafValues(Object target) {
        Map<Object, Object> columnToValueMap = (Map<Object, Object>) ReflectionUtilities.readField(field, target);
        return this.columnToValueMap.keySet().stream()
                .map(key -> new TypedValue(valueType, columnToValueMap.get(key)))
                .toList();
    }
}
