package com.reflectiveexcel.writer.node;

public class TypedValue {

    private final Class<?> type;
    private final Object value;

    public TypedValue(Class<?> type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Class<?> getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
