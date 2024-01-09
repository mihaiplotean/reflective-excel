package com.mihai.deserializer;

import com.mihai.ReadingContext;
import com.mihai.exception.BadInputException;
import com.mihai.workbook.sheet.PropertyCell;

public interface CellDeserializer<T> {

    T deserialize(ReadingContext context, PropertyCell cell) throws BadInputException;
}
