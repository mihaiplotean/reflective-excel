package com.mihai.reader.deserializer;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.exception.BadInputException;
import com.mihai.reader.workbook.sheet.ReadableCell;

public interface CellDeserializer<T> {

    T deserialize(ReadingContext context, ReadableCell cell) throws BadInputException;
}
