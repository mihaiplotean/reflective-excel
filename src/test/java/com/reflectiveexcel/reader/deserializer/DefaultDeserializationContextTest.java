package com.reflectiveexcel.reader.deserializer;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.reflectiveexcel.reader.exception.BadInputException;
import org.junit.jupiter.api.Test;

public class DefaultDeserializationContextTest {

    @Test
    public void missingDeserializerThrowsException() {
        assertThrows(BadInputException.class,
                     () -> new DefaultDeserializationContext().deserialize(null, DefaultDeserializationContextTest.class, null));
    }
}
