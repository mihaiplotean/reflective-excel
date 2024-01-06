package com.mihai.deserializer;

import com.mihai.exception.BadInputException;
import com.mihai.workbook.PropertyCell;

public interface DeserializationContext {

    <T> T deserialize(Class<T> clazz, PropertyCell propertyCell) throws BadInputException;

    <T> void registerDeserializer(Class<T> clazz, CellDeserializer<T> deserializer);
}
