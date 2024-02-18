package com.mihai.deserializer;

import com.mihai.ReadingContext;
import com.mihai.exception.BadInputException;
import com.mihai.workbook.sheet.ReadableCell;

public interface CellDeserializer<T> {

    T deserialize(ReadingContext context, ReadableCell cell) throws BadInputException;
}
