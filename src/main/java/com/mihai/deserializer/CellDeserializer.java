package com.mihai.deserializer;

import com.mihai.ReadingContext;
import com.mihai.exception.BadInputException;
import com.mihai.workbook.PropertyCell;

public interface CellDeserializer<T> {

    T deserialize(ReadingContext context, PropertyCell cell) throws BadInputException;
}
