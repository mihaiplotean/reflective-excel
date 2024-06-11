package com.mihai.reader.deserializer;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.exception.BadInputException;
import com.mihai.reader.workbook.sheet.ReadableCell;

/**
 * Defines how to convert a cell (value) to a given type.
 *
 * @param <T> target type.
 */
public interface CellDeserializer<T> {

    /**
     * Returns the cell value, deserialized.
     *
     * @param context information related to the sheet's reading process.
     * @param cell the cell that should be deserialized.
     * @return the deserialized cell value.
     * @throws BadInputException if the cell value could not be deserialized to the given type. For example, if you would
     * like to deserialize to a Date object, but the value is not a date.
     */
    T deserialize(ReadingContext context, ReadableCell cell) throws BadInputException;
}
