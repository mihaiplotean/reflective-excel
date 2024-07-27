package com.reflectiveexcel.reader.bean;

import java.lang.reflect.Field;
import java.util.List;

import com.reflectiveexcel.core.field.AnnotatedField;
import com.reflectiveexcel.core.field.AnnotatedFieldType;

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
