package com.mihai.reader.field;

import com.mihai.reader.field.value.AnnotatedFieldValue;

import java.lang.reflect.Field;

public interface AnnotatedField {

    Field getField();

    AnnotatedFieldValue newFieldValue();
}
