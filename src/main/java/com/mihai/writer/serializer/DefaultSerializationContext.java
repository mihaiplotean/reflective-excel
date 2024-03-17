package com.mihai.writer.serializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class DefaultSerializationContext implements SerializationContext {

    private final Map<Class<?>, CellSerializer<?>> serializerMap = new HashMap<>();

    public DefaultSerializationContext() {
        registerSerializers();
    }

    protected void registerSerializers() {
        registerSerializer(String.class, CellSerializers.identity());
        registerSerializer(Integer.class, CellSerializers.identity());
        registerSerializer(Long.class, CellSerializers.identity());
        registerSerializer(Double.class, CellSerializers.identity());
        registerSerializer(Float.class, CellSerializers.identity());
        registerSerializer(Boolean.class, CellSerializers.identity());
        registerSerializer(Date.class, CellSerializers.identity());
        registerSerializer(LocalDateTime.class, CellSerializers.identity());
        registerSerializer(LocalDate.class, CellSerializers.identity());
        registerSerializer(Currency.class, CellSerializers.forCurrency());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Object serialize(Class<T> clazz, T value) {
        CellSerializer<T> serializer = (CellSerializer<T>) serializerMap.get(clazz);
        if (serializer == null) {
            return value == null ? "" : value.toString();
        }
        return serializer.serialize(value);
    }

    @Override
    public <T> void registerSerializer(Class<T> clazz, CellSerializer<T> serializer) {
        serializerMap.put(clazz, serializer);
    }
}
