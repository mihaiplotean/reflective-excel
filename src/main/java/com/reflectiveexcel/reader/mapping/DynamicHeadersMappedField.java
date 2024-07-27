package com.reflectiveexcel.reader.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.reflectiveexcel.core.field.DynamicColumnField;
import com.reflectiveexcel.core.utils.ReflectionUtilities;
import com.reflectiveexcel.reader.ReadingContext;
import com.reflectiveexcel.reader.detector.MaybeDynamicColumn;
import com.reflectiveexcel.reader.table.TableHeader;
import com.reflectiveexcel.reader.workbook.sheet.ReadableCell;

public class DynamicHeadersMappedField implements HeaderMappedField {

    private final DynamicColumnField field;

    private Object fieldValue;

    public DynamicHeadersMappedField(DynamicColumnField field) {
        this.field = field;
    }

    @Override
    public DynamicColumnField getField() {
        return field;
    }

    @Override
    public boolean canMapTo(ReadingContext context, TableHeader header) {
        return field.getColumnDetector().test(context, new MaybeDynamicColumn(context, header.getCell()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void storeCurrentValue(ReadingContext readingContext) {
        if (fieldValue == null) {
            fieldValue = getInitialFieldValue();
        }
        Field field = this.field.getField();
        Class<?> type = field.getType();
        if (type == List.class) {
            List<Object> values = (List<Object>) fieldValue;
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> argumentType = (Class<?>) genericType.getActualTypeArguments()[0];
            Object listValue = readingContext.getCurrentCellValue(argumentType);
            values.add(listValue);
        }
        if (type == Map.class) {
            Map<Object, Object> values = (Map<Object, Object>) fieldValue;
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> keyArgumentType = (Class<?>) genericType.getActualTypeArguments()[0];
            Class<?> valueArgumentType = (Class<?>) genericType.getActualTypeArguments()[1];

            ReadableCell headerCell = readingContext.getCurrentTableHeaders().getHeader(readingContext.getCurrentColumnNumber());
            Object mapKey = readingContext.getCellValue(headerCell.getCellReference(), keyArgumentType);
            Object mapValue = readingContext.getCurrentCellValue(valueArgumentType);

            values.put(mapKey, mapValue);
        }
    }

    private Object getInitialFieldValue() {
        Class<?> type = field.getFieldType();
        if (type == List.class) {
            return new ArrayList<>();
        }
        if (type == Map.class) {
            return new LinkedHashMap<>();
        }
        throw new IllegalStateException(String.format("Unsupported field type %s", type.getSimpleName()));
    }

    @Override
    public void writeTo(Object targetObject) {
        ReflectionUtilities.writeField(field.getField(), targetObject, fieldValue);
    }

    @Override
    public void resetValue() {
        fieldValue = null;
    }
}
