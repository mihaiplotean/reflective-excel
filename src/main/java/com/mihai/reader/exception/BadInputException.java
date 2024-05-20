package com.mihai.reader.exception;

import com.mihai.common.ReflectiveExcelException;

public class BadInputException extends ReflectiveExcelException {

    private static final String NO_DESERIALIZER_CLASS_X_MESSAGE = "A deserializer for class \"%s\" has not been registered";

    public BadInputException(String message) {
        super(message);
    }

    public static BadInputException missingDeserializer(Class<?> clazz) {
        return new BadInputException(String.format(NO_DESERIALIZER_CLASS_X_MESSAGE, clazz.getSimpleName()));
    }
}
