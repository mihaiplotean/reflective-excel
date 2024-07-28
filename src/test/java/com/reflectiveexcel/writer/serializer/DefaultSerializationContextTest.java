package com.reflectiveexcel.writer.serializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Date;

import com.reflectiveexcel.core.CellPointer;
import com.reflectiveexcel.writer.WritingContext;
import com.reflectiveexcel.writer.table.TableWritingContext;
import org.junit.jupiter.api.Test;

public class DefaultSerializationContextTest {

    private final WritingContext context = new WritingContext(new TableWritingContext(), new CellPointer());

    @Test
    public void serializedIntPrimitiveIsInputInt() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(12, serializationContext.serialize(context, int.class, 12));
    }

    @Test
    public void serializedBytePrimitiveIsInputByte() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals((byte) 12, serializationContext.serialize(context, byte.class, (byte) 12));
    }

    @Test
    public void serializedShortPrimitiveIsInputShort() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals((short) 12, serializationContext.serialize(context, short.class, (short) 12));
    }

    @Test
    public void serializedLongPrimitiveIsInputLong() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(12L, serializationContext.serialize(context, long.class, 12L));
    }

    @Test
    public void serializedDoublePrimitiveIsInputDouble() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(12d, serializationContext.serialize(context, double.class, 12d));
    }

    @Test
    public void serializedFloatPrimitiveIsInputFloat() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(12f, serializationContext.serialize(context, float.class, 12f));
    }

    @Test
    public void serializedBooleanPrimitiveIsInputBoolean() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(true, serializationContext.serialize(context, boolean.class, true));
    }

    @Test
    public void serializedIntegerIsInputInteger() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(12, serializationContext.serialize(context, Integer.class, 12));
    }

    @Test
    public void serializedByteIsInputByte() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals((byte) 12, serializationContext.serialize(context, Byte.class, (byte) 12));
    }

    @Test
    public void serializedShortIsInputShort() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals((short) 12, serializationContext.serialize(context, Short.class, (short) 12));
    }

    @Test
    public void serializedLongIsInputLong() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(12L, serializationContext.serialize(context, Long.class, 12L));
    }

    @Test
    public void serializedDoubleIsInputDouble() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(12d, serializationContext.serialize(context, Double.class, 12d));
    }

    @Test
    public void serializedFloatIsInputFloat() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(12f, serializationContext.serialize(context, Float.class, 12f));
    }

    @Test
    public void serializedBooleanIsInputBoolean() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(true, serializationContext.serialize(context, Boolean.class, true));
    }

    @Test
    public void serializedDateIsInputDate() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        Date date = new Date();
        assertEquals(date, serializationContext.serialize(context, Date.class, date));
    }

    @Test
    public void serializedLocalDateTimeIsInputLocalDateTime() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        LocalDateTime dateTime = LocalDateTime.of(2020, 10, 2, 1, 1);
        assertEquals(dateTime, serializationContext.serialize(context, LocalDateTime.class, dateTime));
    }

    @Test
    public void serializedLocalDateIsInputLocalDate() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        LocalDate date = LocalDate.of(2020, 10, 2);
        assertEquals(date, serializationContext.serialize(context, LocalDate.class, date));
    }

    @Test
    public void serializedCurrencyIsInputCurrencyCode() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        Currency currency = Currency.getInstance("USD");
        assertEquals("USD", serializationContext.serialize(context, Currency.class, currency));
    }

    @Test
    public void serializedNullIntegerIsNullValue() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertNull(serializationContext.serialize(context, Integer.class, null));
    }

    @Test
    public void serializedStringIsInputString() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals("Hello", serializationContext.serialize(context, String.class, "Hello"));
    }

    @Test
    public void serializedNullStringIsNullValue() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertNull(serializationContext.serialize(context, String.class, null));
    }

    @Test
    public void serializedValueWhenMissingSerializerIsStringRepresentation() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        DefaultSerializationContext value = new DefaultSerializationContext();
        assertEquals(value.toString(), serializationContext.serialize(context, DefaultSerializationContext.class, value));
    }

    @Test
    public void serializedNullValueWhenMissingSerializerIsEmptyString() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals("", serializationContext.serialize(context, DefaultSerializationContext.class, null));
    }

    @Test
    public void serializedBigDecimalIsInputValue() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(BigDecimal.valueOf(12d), serializationContext.serialize(context, BigDecimal.class, BigDecimal.valueOf(12d)));
    }

    @Test
    public void serializedBigIntegerIsInputValue() {
        DefaultSerializationContext serializationContext = new DefaultSerializationContext();
        assertEquals(BigInteger.valueOf(12), serializationContext.serialize(context, BigInteger.class, BigInteger.valueOf(12)));
    }
}
