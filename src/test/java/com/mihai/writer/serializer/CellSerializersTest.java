package com.mihai.writer.serializer;

import org.junit.jupiter.api.Test;

import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class CellSerializersTest {

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
