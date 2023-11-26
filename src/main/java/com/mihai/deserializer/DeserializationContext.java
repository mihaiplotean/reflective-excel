package com.mihai.deserializer;

import com.mihai.CellDetails;

public interface DeserializationContext {

    <T> T deserialize(Class<T> clazz, CellDetails cellDetails) throws DeserializationFailedException;

    <T> void registerDeserializer(Class<T> clazz, CellDeserializer<T> deserializer);
}
