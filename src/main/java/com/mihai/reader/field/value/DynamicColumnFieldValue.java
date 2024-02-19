package com.mihai.reader.field.value;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.ReflectionUtilities;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DynamicColumnFieldValue implements AnnotatedFieldValue {

    private final Field field;

    private Object fieldValue;

    public DynamicColumnFieldValue(Field field) {
        this.field = field;
    }

    @Override
    public void writeTo(Object targetObject) {
        ReflectionUtilities.writeField(field, targetObject, fieldValue);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readValue(ReadingContext context) {
        if (fieldValue == null) {
            fieldValue = getInitialFieldValue();
        }
        Class<?> type = field.getType();
        if (type == List.class) {
            List<Object> values = (List<Object>) fieldValue;
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> argumentType = (Class<?>) genericType.getActualTypeArguments()[0];  // todo: unsafe cast?
            Object listValue = context.getCurrentCellValue(argumentType);
            values.add(listValue);
        }
        if (type == Map.class) {
            Map<Object, Object> values = (Map<Object, Object>) fieldValue;
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> keyArgumentType = (Class<?>) genericType.getActualTypeArguments()[0];  // todo: unsafe cast?
            Class<?> valueArgumentType = (Class<?>) genericType.getActualTypeArguments()[1];  // todo: unsafe cast?

            ReadableCell headerCell = context.getCurrentTableHeaders().getHeader(context.getCurrentColumnNumber());
            Object mapKey = context.getCellValue(headerCell.getCellReference(), keyArgumentType);
            Object mapValue = context.getCurrentCellValue(valueArgumentType);

            values.put(mapKey, mapValue);
        }
    }

    private Object getInitialFieldValue() {
        Class<?> type = field.getType();
        if (type == List.class) {
            return new ArrayList<>();
        }
        if (type == Map.class) {
            return new LinkedHashMap<>();
        }
        throw new IllegalStateException(String.format("Unsupported field type %s", type.getSimpleName()));
    }
}
