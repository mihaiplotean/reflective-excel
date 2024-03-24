package com.mihai.writer.serializer;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DefaultSerializationContextTest {

    @Test
    public void serializedIntPrimitiveIsInputInt() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(12, serializationContext.serialize(int.class, 12));
    }

    @Test
    public void serializedBytePrimitiveIsInputByte() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals((byte) 12, serializationContext.serialize(byte.class, (byte) 12));
    }

    @Test
    public void serializedShortPrimitiveIsInputShort() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals((short) 12, serializationContext.serialize(short.class, (short) 12));
    }

    @Test
    public void serializedLongPrimitiveIsInputLong() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(12L, serializationContext.serialize(long.class, 12L));
    }

    @Test
    public void serializedDoublePrimitiveIsInputDouble() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(12d, serializationContext.serialize(double.class, 12d));
    }

    @Test
    public void serializedFloatPrimitiveIsInputFloat() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(12f, serializationContext.serialize(float.class, 12f));
    }

    @Test
    public void serializedBooleanPrimitiveIsInputBoolean() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(true, serializationContext.serialize(boolean.class, true));
    }

    @Test
    public void serializedIntegerIsInputInteger() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(12, serializationContext.serialize(Integer.class, 12));
    }

    @Test
    public void serializedByteIsInputByte() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals((byte) 12, serializationContext.serialize(Byte.class, (byte) 12));
    }

    @Test
    public void serializedShortIsInputShort() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals((short) 12, serializationContext.serialize(Short.class, (short) 12));
    }

    @Test
    public void serializedLongIsInputLong() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(12L, serializationContext.serialize(Long.class, 12L));
    }

    @Test
    public void serializedDoubleIsInputDouble() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(12d, serializationContext.serialize(Double.class, 12d));
    }

    @Test
    public void serializedFloatIsInputFloat() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(12f, serializationContext.serialize(Float.class, 12f));
    }

    @Test
    public void serializedBooleanIsInputBoolean() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(true, serializationContext.serialize(Boolean.class, true));
    }

    @Test
    public void serializedDateIsInputDate() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        Date date = new Date();
        assertEquals(date, serializationContext.serialize(Date.class, date));
    }

    @Test
    public void serializedLocalDateTimeIsInputLocalDateTime() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        LocalDateTime dateTime = LocalDateTime.of(2020, 10, 2, 1, 1);
        assertEquals(dateTime, serializationContext.serialize(LocalDateTime.class, dateTime));
    }

    @Test
    public void serializedLocalDateIsInputLocalDate() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        LocalDate date = LocalDate.of(2020, 10, 2);
        assertEquals(date, serializationContext.serialize(LocalDate.class, date));
    }

    @Test
    public void serializedCurrencyIsInputCurrencyCode() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        Currency currency = Currency.getInstance("USD");
        assertEquals("USD", serializationContext.serialize(Currency.class, currency));
    }

    @Test
    public void serializedNullIntegerIsNullValue() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertNull(serializationContext.serialize(Integer.class, null));
    }

    @Test
    public void serializedStringIsInputString() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals("Hello", serializationContext.serialize(String.class, "Hello"));
    }

    @Test
    public void serializedNullStringIsNullValue() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertNull(serializationContext.serialize(String.class, null));
    }

    @Test
    public void serializedValueWhenMissingSerializerIsStringRepresentation() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        DefaultSerializationContext value = new DefaultSerializationContext();
        assertEquals(value.toString(), serializationContext.serialize(DefaultSerializationContext.class, value));
    }

    @Test
    public void serializedNullValueWhenMissingSerializerIsEmptyString() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals("", serializationContext.serialize(DefaultSerializationContext.class, null));
    }
}
