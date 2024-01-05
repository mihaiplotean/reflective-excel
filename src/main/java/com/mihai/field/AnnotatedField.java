package com.mihai.field;

import com.mihai.field.value.AnnotatedFieldValue;

import java.lang.reflect.Field;

public interface AnnotatedField {

    Field getField();

    AnnotatedFieldValue newFieldValue();
}
