package com.reflectiveexcel.writer.serializer;

import java.util.Currency;

public class CellSerializers {

    private CellSerializers() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Serializer that simply returns the input value.
     */
    public static <T> CellSerializer<T> identity() {
        return (context, value) -> value;
    }

    /**
     * Currency serializer. The currency is serializer to its currency code.
     * For example, the USD currency is converted to "USD";
     */
    public static CellSerializer<Currency> forCurrency() {
        return (context, currency) -> currency.getCurrencyCode();
    }
}
