package com.mihai.writer.node;

import com.mihai.common.utils.ReflectionUtilities;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class FixedFieldNode implements AnnotatedFieldNode {

    private final Field field;
    private final String name;

    public FixedFieldNode(Field field, String name) {
        this.field = field;
        this.name = name;
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
    public Class<?> getType() {
        return field.getType();
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
    public List<AnnotatedFieldNode> getChildren() {
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
}
