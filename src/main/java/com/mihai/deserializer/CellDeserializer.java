package com.mihai.deserializer;

import com.mihai.exception.BadInputException;
import com.mihai.PropertyCell;

public interface CellDeserializer<T> {

    T deserialize(PropertyCell propertyCell) throws BadInputException;
}
