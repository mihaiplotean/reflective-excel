package com.mihai.field.value;

import com.mihai.ReadingContext;
import com.mihai.exception.BadInputException;

public interface AnnotatedFieldValue {

    void writeTo(Object targetObject);

    void readValue(ReadingContext context) throws BadInputException;
}
