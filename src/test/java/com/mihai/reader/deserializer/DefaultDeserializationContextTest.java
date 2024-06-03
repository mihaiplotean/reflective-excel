package com.mihai.reader.deserializer;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mihai.reader.exception.BadInputException;
import org.junit.jupiter.api.Test;

class DefaultDeserializationContextTest {

    @Test
    public void missingDeserializerThrowsException() {
        assertThrows(BadInputException.class,
                     () -> new DefaultDeserializationContext().deserialize(null, DefaultDeserializationContextTest.class, null));
    }
}
