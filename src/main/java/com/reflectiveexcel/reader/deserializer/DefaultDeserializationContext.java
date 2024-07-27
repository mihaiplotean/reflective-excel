package com.reflectiveexcel.reader.deserializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.reflectiveexcel.reader.ReadingContext;
import com.reflectiveexcel.reader.exception.BadInputException;
import com.reflectiveexcel.reader.workbook.sheet.ReadableCell;

/**
 * Provides a default implementation for the deserialization of the most used java primitive types,
 * their corresponding wrapper classes and some other types, as defined in {@link #registerDeserializers()}.
 * You are encouraged to extend this class if you would like to provide more, or replace some of the existing deserializers.
 */
public class DefaultDeserializationContext implements DeserializationContext {

    private final Map<Class<?>, CellDeserializer<?>> deserializerMap = new HashMap<>();

    public DefaultDeserializationContext() {
        registerDeserializers();
    }

    protected void registerDeserializers() {
        registerDeserializer(int.class, CellDeserializers.forInteger());
        registerDeserializer(long.class, CellDeserializers.forLong());
        registerDeserializer(double.class, CellDeserializers.forDouble());
        registerDeserializer(float.class, CellDeserializers.forFloat());
        registerDeserializer(boolean.class, CellDeserializers.forBoolean());

        registerDeserializer(String.class, CellDeserializers.forString());
        registerDeserializer(Integer.class, CellDeserializers.forInteger());
        registerDeserializer(Long.class, CellDeserializers.forLong());
        registerDeserializer(Double.class, CellDeserializers.forDouble());
        registerDeserializer(Float.class, CellDeserializers.forFloat());
        registerDeserializer(Boolean.class, CellDeserializers.forBoolean());
        registerDeserializer(Currency.class, CellDeserializers.forCurrency());
        registerDeserializer(Date.class, CellDeserializers.forDate());
        registerDeserializer(LocalDateTime.class, CellDeserializers.forLocalDateTime());
        registerDeserializer(LocalDate.class, CellDeserializers.forLocalDate());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(ReadingContext context, Class<T> clazz, ReadableCell cell) throws BadInputException {
        CellDeserializer<?> cellDeserializer = deserializerMap.get(clazz);
        if (cellDeserializer == null) {
            throw BadInputException.missingDeserializer(clazz);
        }
        return (T) cellDeserializer.deserialize(context, cell);
    }

    @Override
    public <T> void registerDeserializer(Class<T> clazz, CellDeserializer<T> deserializer) {
        deserializerMap.put(clazz, deserializer);
    }
}
