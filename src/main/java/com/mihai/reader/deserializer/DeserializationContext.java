package com.mihai.reader.deserializer;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.exception.BadInputException;
import com.mihai.reader.workbook.sheet.ReadableCell;

public interface DeserializationContext {

    <T> T deserialize(ReadingContext context, Class<T> clazz, ReadableCell cell) throws BadInputException;

    <T> void registerDeserializer(Class<T> clazz, CellDeserializer<T> deserializer);
}
