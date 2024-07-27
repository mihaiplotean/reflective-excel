package com.reflectiveexcel.writer.serializer;

import com.reflectiveexcel.writer.WritingContext;

/**
 * Defines how to convert a given type to a cell value.
 *
 * @param <T> target type.
 */
public interface CellSerializer<T> {

    Object serialize(WritingContext context, T value);
}
