package com.mihai.reader.field;

import com.mihai.reader.field.value.AnnotatedFieldValue;
import com.mihai.reader.field.value.KeyValueFieldValue;

import java.lang.reflect.Field;

public class KeyValueField implements AnnotatedField {

    private final String propertyName;
    private final String nameReference;
    private final String valueReference;

    private final Field field;

    public KeyValueField(String propertyName, String nameReference, String valueReference, Field field) {
        this.propertyName = propertyName;
        this.nameReference = nameReference;
        this.valueReference = valueReference;
        this.field = field;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AnnotatedFieldValue newFieldValue() {
        return new KeyValueFieldValue(propertyName, nameReference, valueReference, field);
    }
}
