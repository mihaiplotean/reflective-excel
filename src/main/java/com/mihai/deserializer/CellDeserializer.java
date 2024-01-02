package com.mihai.deserializer;

import com.mihai.exception.BadInputException;
import com.mihai.ExcelCell;

public interface CellDeserializer<T> {

    T deserialize(ExcelCell excelCell) throws BadInputException;
}
