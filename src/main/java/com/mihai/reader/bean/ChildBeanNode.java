package com.mihai.reader.bean;

import com.mihai.reader.field.AnnotatedField;
import com.mihai.reader.field.AnnotatedFieldType;

import java.lang.reflect.Field;
import java.util.List;

public interface ChildBeanNode {

    Field getField();

    String getName();

    default Class<?> getType() {
        return getField().getType();
    }

    int getLength();

    int getHeight();

    List<? extends ChildBeanNode> getChildren();

    List<? extends ChildBeanNode> getLeaves();

    AnnotatedFieldType getAnnotatedFieldType();
}
