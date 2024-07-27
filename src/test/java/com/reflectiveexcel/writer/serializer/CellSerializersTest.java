package com.reflectiveexcel.writer.serializer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Currency;

import com.reflectiveexcel.core.CellPointer;
import com.reflectiveexcel.writer.WritingContext;
import com.reflectiveexcel.writer.table.TableWritingContext;
import org.junit.jupiter.api.Test;

public class CellSerializersTest {

    @Test
    public void serializedValueUsingIdentitySerializerIsEqualToInput() {
        CellSerializer<Object> serializer = CellSerializers.identity();
        Object value = new Object();
        WritingContext context = new WritingContext(new TableWritingContext(), new CellPointer());
        assertEquals(value, serializer.serialize(context, value));
    }

    @Test
    public void serializedCurrencyIsCurrencyCode() {
        CellSerializer<Currency> serializer = CellSerializers.forCurrency();
        Currency usdCurrency = Currency.getInstance("USD");
        WritingContext context = new WritingContext(new TableWritingContext(), new CellPointer());
        assertEquals("USD", serializer.serialize(context, usdCurrency));
    }
}
