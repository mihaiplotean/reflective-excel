package com.mihai.writer.serializer;

public interface CellSerializer<T> {

    Object serialize(T value);
}
