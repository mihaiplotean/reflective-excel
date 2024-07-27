package com.reflectiveexcel.writer.node;

import java.lang.reflect.Field;
import java.util.List;

public interface ChildBeanWriteNode {

    Object getName();

    Field getField();

    int getLength();

    int getHeight();

    List<? extends ChildBeanWriteNode> getChildren();

    List<TypedValue> getLeafValues(Object target);

    boolean isLeafValue();
}
