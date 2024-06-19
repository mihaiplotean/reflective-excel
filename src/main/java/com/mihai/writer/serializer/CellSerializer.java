package com.mihai.writer.serializer;

/**
 * Defines how to convert a given type to a cell value.
 *
 * @param <T> target type.
 */
public interface CellSerializer<T> {

    Object serialize(T value);  // todo: add writingcontext?
}
