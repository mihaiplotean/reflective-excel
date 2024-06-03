package com.mihai.core.field;

import java.lang.reflect.Field;

import org.apache.poi.ss.util.CellReference;

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

    @Override
    public AnnotatedFieldType getType() {
        return AnnotatedFieldType.KEY_VALUE;
    }

    public String getNameReference() {
        return nameReference;
    }

    public String getValueReference() {
        return valueReference;
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
}
