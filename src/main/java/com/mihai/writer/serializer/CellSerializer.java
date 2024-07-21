package com.mihai.writer.serializer;

import com.mihai.writer.WritingContext;

/**
 * Defines how to convert a given type to a cell value.
 *
 * @param <T> target type.
 */
public interface CellSerializer<T> {

    Object serialize(WritingContext context, T value);
}
