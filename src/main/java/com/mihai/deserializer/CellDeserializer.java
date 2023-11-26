package com.mihai.deserializer;

import com.mihai.CellDetails;

public interface CellDeserializer<T> {

    T deserialize(CellDetails cellDetails) throws DeserializationFailedException;
}
