package com.mihai.deserializer;

import com.mihai.ExcelCell;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DefaultDeserializationContext implements DeserializationContext {

    private final Map<Class<?>, CellDeserializer<?>> deserializerMap = new HashMap<>();

    public DefaultDeserializationContext() {
        registerDeserializers();
    }

    protected void registerDeserializers() {
        registerDeserializer(String.class, CellDeserializers.forString());
        registerDeserializer(Integer.class, CellDeserializers.forInteger());
        registerDeserializer(Long.class, CellDeserializers.forLong());
        registerDeserializer(Double.class, CellDeserializers.forDouble());
        registerDeserializer(Float.class, CellDeserializers.forFloat());
        registerDeserializer(Boolean.class, CellDeserializers.forBoolean());
        registerDeserializer(Currency.class, CellDeserializers.forCurrency());
        registerDeserializer(Date.class, CellDeserializers.forDate());
        registerDeserializer(LocalDateTime.class, CellDeserializers.forLocalDateTime());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(Class<T> clazz, ExcelCell excelCell) throws DeserializationFailedException {
        CellDeserializer<?> cellDeserializer = deserializerMap.get(clazz);
        if (cellDeserializer == null) {
            throw DeserializationFailedException.missingDeserializer(clazz);
        }
        return (T) cellDeserializer.deserialize(excelCell);
    }

    @Override
    public <T> void registerDeserializer(Class<T> clazz, CellDeserializer<T> deserializer) {
        deserializerMap.put(clazz, deserializer);
    }
}
