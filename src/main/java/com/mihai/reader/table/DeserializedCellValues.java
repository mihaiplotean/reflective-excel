package com.mihai.reader.table;

import java.util.HashMap;
import java.util.Map;

public class DeserializedCellValues {

    private final Map<DeserializedCellValue, Object> valueCache = new HashMap<>();

    public <T> void putValue(int row, int colum, Class<T> type, T value) {
        DeserializedCellValue valueReference = new DeserializedCellValue(row, colum, type);
        valueCache.put(valueReference, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(int row, int colum, Class<T> type) {
        DeserializedCellValue valueReference = new DeserializedCellValue(row, colum, type);
        return (T) valueCache.get(valueReference);
    }

    private record DeserializedCellValue(int row, int column, Class<?> valueType) {

    }
}
