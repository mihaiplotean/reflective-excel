package com.mihai.writer.serializer;

import com.mihai.writer.serializer.CellSerializer;

public interface SerializationContext {

    <T> Object serialize(Class<T> clazz, T value);

    <T> void registerSerializer(Class<T> clazz, CellSerializer<T> serializer);
}
