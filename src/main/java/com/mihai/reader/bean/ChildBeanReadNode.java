package com.mihai.reader.bean;

import java.lang.reflect.Field;
import java.util.List;

import com.mihai.core.field.AnnotatedField;
import com.mihai.core.field.AnnotatedFieldType;

public interface ChildBeanReadNode {

    AnnotatedField getAnnotatedField();

    String getName();

    default Field getField() {
        return getAnnotatedField().getField();
    }

    List<ChildBeanReadNode> getChildren();

    List<ChildBeanReadNode> getLeaves();

    AnnotatedFieldType getAnnotatedFieldType();
}
