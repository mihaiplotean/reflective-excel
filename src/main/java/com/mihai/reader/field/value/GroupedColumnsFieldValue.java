package com.mihai.reader.field.value;

import com.mihai.reader.ColumnFieldMapping;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.exception.BadInputException;
import com.mihai.reader.field.AnnotatedField;
import com.mihai.ReflectionUtilities;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GroupedColumnsFieldValue implements AnnotatedFieldValue {

    private final Field field;
    private final ColumnFieldMapping columnFieldMapping;
    private final Map<AnnotatedField, AnnotatedFieldValue> groupFieldValues;

    public GroupedColumnsFieldValue(Field field, ColumnFieldMapping columnFieldMapping) {
        this.field = field;
        this.columnFieldMapping = columnFieldMapping;
        this.groupFieldValues = new HashMap<>();
    }

    @Override
    public void writeTo(Object targetObject) {
        Object cellGroupObject = ReflectionUtilities.newObject(field.getType());
        groupFieldValues.values().forEach(value -> value.writeTo(cellGroupObject));
        ReflectionUtilities.writeField(field, targetObject, cellGroupObject);
    }

    @Override
    public void readValue(ReadingContext context) throws BadInputException {
        AnnotatedField field = columnFieldMapping.getField(context.getCurrentCell().getColumnNumber());
        if (field != null) {
            AnnotatedFieldValue fieldValue = groupFieldValues.computeIfAbsent(field, AnnotatedField::newFieldValue);
            fieldValue.readValue(context);
        }
    }
}
