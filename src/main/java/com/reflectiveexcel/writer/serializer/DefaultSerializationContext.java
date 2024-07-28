package com.reflectiveexcel.writer.serializer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.reflectiveexcel.writer.WritingContext;

/**
 * Provides a default implementation for the serialization of the most used java primitive types,
 * their corresponding wrapper classes and some other types, as defined in {@link #registerSerializers()}.
 * You are encouraged to extend this class if you would like to provide more, or replace some of the existing serializers.
 */
public class DefaultSerializationContext implements SerializationContext {

    private final Map<Class<?>, CellSerializer<?>> serializerMap = new HashMap<>();

    public DefaultSerializationContext() {
        registerSerializers();
    }

    protected void registerSerializers() {
        registerSerializer(int.class, CellSerializers.identity());
        registerSerializer(byte.class, CellSerializers.identity());
        registerSerializer(short.class, CellSerializers.identity());
        registerSerializer(long.class, CellSerializers.identity());
        registerSerializer(double.class, CellSerializers.identity());
        registerSerializer(float.class, CellSerializers.identity());
        registerSerializer(boolean.class, CellSerializers.identity());
        registerSerializer(String.class, CellSerializers.identity());
        registerSerializer(Integer.class, CellSerializers.identity());
        registerSerializer(Byte.class, CellSerializers.identity());
        registerSerializer(Short.class, CellSerializers.identity());
        registerSerializer(Long.class, CellSerializers.identity());
        registerSerializer(Double.class, CellSerializers.identity());
        registerSerializer(Float.class, CellSerializers.identity());
        registerSerializer(Boolean.class, CellSerializers.identity());
        registerSerializer(Date.class, CellSerializers.identity());
        registerSerializer(LocalDateTime.class, CellSerializers.identity());
        registerSerializer(LocalDate.class, CellSerializers.identity());
        registerSerializer(Currency.class, CellSerializers.forCurrency());
        registerSerializer(BigDecimal.class, CellSerializers.identity());
        registerSerializer(BigInteger.class, CellSerializers.identity());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Object serialize(WritingContext context, Class<T> clazz, T value) {
        CellSerializer<T> serializer = (CellSerializer<T>) serializerMap.get(clazz);
        if (serializer == null) {
            return value == null ? "" : value.toString();
        }
        return serializer.serialize(context, value);
    }

    @Override
    public <T> void registerSerializer(Class<T> clazz, CellSerializer<T> serializer) {
        serializerMap.put(clazz, serializer);
    }
}
