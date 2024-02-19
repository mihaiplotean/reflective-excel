package com.mihai.reader.field.value;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.exception.BadInputException;

public interface AnnotatedFieldValue {

    void writeTo(Object targetObject);

    void readValue(ReadingContext context) throws BadInputException;
}
