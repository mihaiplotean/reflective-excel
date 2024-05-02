package com.mihai.reader.deserializer;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.exception.BadInputException;
import com.mihai.reader.workbook.sheet.ReadableCell;

import java.time.LocalDate;
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
