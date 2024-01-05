package com.mihai.field.value;

import com.mihai.ReadingContext;
import com.mihai.ReflectionUtilities;
import com.mihai.exception.BadInputException;

import java.lang.reflect.Field;

public class KeyValueFieldValue implements AnnotatedFieldValue {

    private final String propertyName;
    private final String nameReference;
    private final String valueReference;
    private final Field field;

    private Object fieldValue;

    public KeyValueFieldValue(String propertyName, String nameReference, String valueReference, Field field) {
        this.propertyName = propertyName;
        this.nameReference = nameReference;
        this.valueReference = valueReference;
        this.field = field;
    }

    @Override
    public void writeTo(Object targetObject) {
        ReflectionUtilities.writeField(field, targetObject, fieldValue);
    }

    @Override
    public void readValue(ReadingContext context) {
        String actualPropertyName = context.getCellValue(nameReference);
        if (!propertyName.equalsIgnoreCase(actualPropertyName)) {
            throw new BadInputException(String.format(
                    "Property name \"%s\" at cell %s does not match the one expected %s".formatted(
                            actualPropertyName, nameReference, propertyName
                    ))
            );
        }
        fieldValue = context.getCellValue(valueReference, field.getType());
    }
}
