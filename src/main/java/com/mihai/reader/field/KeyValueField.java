package com.mihai.reader.field;

import com.mihai.reader.field.value.AnnotatedFieldValue;
import com.mihai.reader.field.value.KeyValueFieldValue;
import org.apache.poi.ss.util.CellReference;

import java.lang.reflect.Field;

public class KeyValueField implements AnnotatedField {

    private final String propertyName;
    private final String nameReference;
    private final String valueReference;

    private final Field field;

    public KeyValueField(String propertyName, String nameReference, String valueReference, Field field) {
        this.propertyName = propertyName;
        this.nameReference = nameReference;
        this.valueReference = valueReference;
        this.field = field;
    }

    @Override
    public Field getField() {
        return field;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public int getPropertyRow() {
        return new CellReference(nameReference).getRow();
    }

    public int getPropertyColumn() {
        return new CellReference(nameReference).getCol();
    }

    public int getValueRow() {
        return new CellReference(valueReference).getRow();
    }

    public int getValueColumn() {
        return new CellReference(valueReference).getCol();
    }

    @Override
    public AnnotatedFieldValue newFieldValue() {
        return new KeyValueFieldValue(propertyName, nameReference, valueReference, field);
    }
}
