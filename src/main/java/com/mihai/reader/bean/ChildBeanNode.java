package com.mihai.reader.bean;

import com.mihai.reader.field.AnnotatedField;
import com.mihai.reader.field.AnnotatedFieldType;

import java.lang.reflect.Field;
import java.util.List;

public interface ChildBeanNode {

    AnnotatedField getAnnotatedField();

    String getName();

    default Field getField() {
        return getAnnotatedField().getField();
    }

    default Class<?> getType() {
        return getAnnotatedField().getFieldType();
    }

    int getLength();

    int getHeight();

    List<ChildBeanNode> getChildren();

    List<ChildBeanNode> getLeaves();

    AnnotatedFieldType getAnnotatedFieldType();
}
