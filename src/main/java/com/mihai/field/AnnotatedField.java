package com.mihai.field;

import com.mihai.field.value.AnnotatedFieldValue;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public interface AnnotatedField {

    Field getField();

    AnnotatedFieldValue newFieldValue();
}
