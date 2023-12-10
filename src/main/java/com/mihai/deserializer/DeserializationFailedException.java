package com.mihai.deserializer;

public class DeserializationFailedException extends RuntimeException {

    private static final String NO_DESERIALIZER_CLASS_X_MESSAGE = "A deserializer for class \"%s\" has not been registered";

    public DeserializationFailedException(String message) {
        super(message);
    }

    public static DeserializationFailedException missingDeserializer(Class<?> clazz) {
        return new DeserializationFailedException(String.format(NO_DESERIALIZER_CLASS_X_MESSAGE, clazz.getSimpleName()));
    }
}
