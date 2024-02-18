package com.mihai.writer.node;

import com.mihai.ReflectionUtilities;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GroupedFieldNode implements AnnotatedFieldNodeInterface {

    private final Field field;
    private final Object target;
    private final String name;
    private final List<AnnotatedFieldNodeInterface> children;

    public GroupedFieldNode(Field field, Object target, String name) {
        this.field = field;
        this.target = target;
        this.name = name;
        this.children = new AnnotatedFieldNodeCreator(field.getType(), target).getChildFields();
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
        return children.stream()
                .mapToInt(AnnotatedFieldNodeInterface::getLength)
                .sum();
    }

    @Override
    public int getHeight() {
        return 1 + children.stream()
                .mapToInt(AnnotatedFieldNodeInterface::getHeight)
                .max()
                .orElse(0);
    }

    @Override
    public List<AnnotatedFieldNodeInterface> getChildren() {
        return children;
    }

    @Override
    public List<TypedValue> getLeafValues(Object row) {
        return children.stream()
                .map(child -> child.getLeafValues(target))
                .flatMap(Collection::stream)
                .toList();
    }
}
