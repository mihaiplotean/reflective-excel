package com.mihai.deserializer;

import com.mihai.ExcelCell;

public interface CellDeserializer<T> {

    T deserialize(ExcelCell excelCell) throws DeserializationFailedException;
}
