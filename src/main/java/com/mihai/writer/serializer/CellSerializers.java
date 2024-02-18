package com.mihai.writer.serializer;

import java.util.Currency;

public class CellSerializers {

    private CellSerializers() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> CellSerializer<T> identity() {
        return value -> value;
    }

    public static CellSerializer<Currency> forCurrency() {
        return Currency::getCurrencyCode;
    }
}
