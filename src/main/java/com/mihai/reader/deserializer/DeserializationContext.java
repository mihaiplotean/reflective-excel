package com.mihai.reader.deserializer;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.exception.BadInputException;
import com.mihai.reader.workbook.sheet.ReadableCell;

/**
 * Class responsible for storing the deserializers and the deserialization process itself.
 */
public interface DeserializationContext {

    /**
     * Converts a cell (value) to a given type, using the registered deserializer.
     *
     * @param context information related to the sheet's reading process.
     * @param clazz   the type to deserialize to.
     * @param cell    the cell that should be deserialized.
     * @return the deserialized cell value.
     * @throws BadInputException if the cell value could not be deserialized to the given type. For example, if you would
     *                           like to deserialize to a Date object, but the value is not a date.
     */
    <T> T deserialize(ReadingContext context, Class<T> clazz, ReadableCell cell) throws BadInputException;

    /**
     * Registers a deserializer for the given type.
     *
     * @param clazz        the type that the deserializer is intended for.
     * @param deserializer the deserializer itself.
     */
    <T> void registerDeserializer(Class<T> clazz, CellDeserializer<T> deserializer);
}
