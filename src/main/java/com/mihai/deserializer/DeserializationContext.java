package com.mihai.deserializer;

import com.mihai.ExcelCell;

public interface DeserializationContext {

    <T> T deserialize(Class<T> clazz, ExcelCell excelCell) throws DeserializationFailedException;

    <T> void registerDeserializer(Class<T> clazz, CellDeserializer<T> deserializer);
}
