package com.mihai.writer.serializer;

import com.mihai.writer.serializer.CellSerializer;

public interface SerializationContext {

    <T> Object serialize(Class<T> clazz, T value);  // value can be null, that's why we can't do value.getClass

    <T> void registerSerializer(Class<T> clazz, CellSerializer<T> serializer);
}
