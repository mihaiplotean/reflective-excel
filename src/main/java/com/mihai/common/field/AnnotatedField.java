package com.mihai.common.field;

import java.lang.reflect.Field;

public interface AnnotatedField {

    Field getField();

    AnnotatedFieldType getType();

    default Class<?> getFieldType() {
        return getField().getType();
    }
}
