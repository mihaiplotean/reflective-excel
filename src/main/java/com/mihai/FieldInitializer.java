package com.mihai;

import com.mihai.deserializer.DeserializationContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class FieldInitializer {

    private final ColumnProperty columnProperty;

    public FieldInitializer(ColumnProperty columnProperty) {
        this.columnProperty = columnProperty;
    }

    public Object getInitializeField(DeserializationContext deserializationContext, ExcelCell cell) {
        if(columnProperty.isDynamic()) {
            return getInitializedDynamicColumField(deserializationContext, cell);
        }
        return deserializationContext.deserialize(columnProperty.getFieldType(), cell);
    }

    public Object getInitializedDynamicColumField(DeserializationContext deserializationContext, ExcelCell cell) {
//        Class<?> fieldType = columnProperty.getFieldType();
//        if(fieldType.isAssignableFrom(ArrayList.class)) {
//
//        }
//        if(fieldType.isAssignableFrom(LinkedHashMap.class)) {
//            deserializationContext.deserialize()
//        }
        return null;
    }
}
