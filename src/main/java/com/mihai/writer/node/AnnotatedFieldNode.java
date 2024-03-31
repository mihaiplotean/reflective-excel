package com.mihai.writer.node;

import java.lang.reflect.Field;
import java.util.List;

public interface AnnotatedFieldNode {

    Object getName();

    Field getField();

    Class<?> getType();

    int getLength();

    int getHeight();

    List<? extends AnnotatedFieldNode> getChildren();

    List<TypedValue> getLeafValues(Object target);

    boolean isLeafValue();
}
