package com.mihai.reader.bean;

import com.mihai.core.field.AnnotatedField;
import com.mihai.core.field.AnnotatedFieldType;

import java.lang.reflect.Field;
import java.util.List;

public interface ChildBeanReadNode {

    AnnotatedField getAnnotatedField();

    String getName();

    default Field getField() {
        return getAnnotatedField().getField();
    }

    default Class<?> getType() {
        return getAnnotatedField().getFieldType();
    }

    List<ChildBeanReadNode> getChildren();

    List<ChildBeanReadNode> getLeaves();

    AnnotatedFieldType getAnnotatedFieldType();
}
