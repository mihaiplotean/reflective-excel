package com.mihai.reader.bean;

import com.mihai.common.field.AnnotatedField;
import com.mihai.common.field.AnnotatedFieldType;

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

    List<ChildBeanNode> getChildren();

    List<ChildBeanNode> getLeaves();

    AnnotatedFieldType getAnnotatedFieldType();
}
