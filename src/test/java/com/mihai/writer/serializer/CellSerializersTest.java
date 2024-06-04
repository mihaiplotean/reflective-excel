package com.mihai.writer.serializer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Currency;

import org.junit.jupiter.api.Test;

public class CellSerializersTest {

    @Test
    public void serializedValueUsingIdentitySerializerIsEqualToInput() {
        CellSerializer<Object> serializer = CellSerializers.identity();
        Object value = new Object();
        assertEquals(value, serializer.serialize(value));
    }

    @Test
    public void serializedCurrencyIsCurrencyCode() {
        CellSerializer<Currency> serializer = CellSerializers.forCurrency();
        Currency usdCurrency = Currency.getInstance("USD");
        assertEquals("USD", serializer.serialize(usdCurrency));
    }
}
