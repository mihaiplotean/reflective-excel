package com.mihai.reader.field.value;

import com.mihai.*;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.RowColumnDetector;
import com.mihai.reader.RowReader;
import com.mihai.reader.detector.ColumnDetector;
import com.mihai.reader.detector.RowDetector;
import com.mihai.reader.exception.BadInputException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;

public class RowsFieldValue implements AnnotatedFieldValue {

    private final RowReader rowReader;
    private final Field field;

    private final RowDetector lastRowDetector;
    private final RowDetector headerRowDetector;
    private final RowDetector skipRowDetector;
    private final ColumnDetector headerStartColumnDetector;
    private final ColumnDetector headerLastColumnDetector;

    private List<?> fieldValue = Collections.emptyList();

    public RowsFieldValue(RowReader rowReader, Field field,
                          RowDetector lastRowDetector,
                          RowDetector headerRowDetector,
                          RowDetector skipRowDetector,
                          ColumnDetector headerStartColumnDetector,
                          ColumnDetector headerLastColumnDetector) {
        this.rowReader = rowReader;
        this.field = field;
        this.lastRowDetector = lastRowDetector;
        this.headerRowDetector = headerRowDetector;
        this.skipRowDetector = skipRowDetector;
        this.headerStartColumnDetector = headerStartColumnDetector;
        this.headerLastColumnDetector = headerLastColumnDetector;
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
            fieldValue = rowReader.readRows(argumentType, createRowColumnDetector(context));
        }
        else {
            throw new IllegalStateException("Only List.class can be annotated as row value");
        }
    }

    private RowColumnDetector createRowColumnDetector(ReadingContext context) {
        return RowColumnDetector.builder(context)
                .lastRowDetector(lastRowDetector)
                .headerRowDetector(headerRowDetector)
                .skipRowDetector(skipRowDetector)
                .headerStartColumnDetector(headerStartColumnDetector)
                .headerLastColumnDetector(headerLastColumnDetector)
                .build();
    }
}
