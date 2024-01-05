package com.mihai.field.value;

import com.mihai.ReadingContext;
import com.mihai.ReflectionUtilities;
import com.mihai.RowReader;
import com.mihai.exception.BadInputException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RowsFieldValue implements AnnotatedFieldValue {

    private final RowReader rowReader;
    private final Field field;

    private List<?> fieldValue = Collections.emptyList();

    public RowsFieldValue(RowReader rowReader, Field field) {
        this.rowReader = rowReader;
        this.field = field;
    }

    @Override
    public void writeTo(Object targetObject) {
        ReflectionUtilities.writeField(field, targetObject, List.copyOf(fieldValue));
    }

    @Override
    public void readValue(ReadingContext context) throws BadInputException {
        Class<?> type = field.getType();
        if (type == List.class) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> argumentType = (Class<?>) genericType.getActualTypeArguments()[0];  // todo: unsafe cast?
            fieldValue = rowReader.readRows(argumentType);
        }
        else {
            throw new IllegalStateException("Only List.class can be annotated as row value");
        }
    }
}
