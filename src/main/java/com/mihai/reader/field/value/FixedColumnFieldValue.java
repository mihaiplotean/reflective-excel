package com.mihai.reader.field.value;

import com.mihai.reader.ReadingContext;
import com.mihai.ReflectionUtilities;

import java.lang.reflect.Field;

public class FixedColumnFieldValue implements AnnotatedFieldValue {

    private final Field field;

    private Object fieldValue;

    public FixedColumnFieldValue(Field field) {
        this.field = field;
    }

    @Override
    public void writeTo(Object targetObject) {
        ReflectionUtilities.writeField(field, targetObject, fieldValue);
    }

    @Override
    public void readValue(ReadingContext context) {
        fieldValue = context.getCurrentCellValue(field.getType());
    }
}
