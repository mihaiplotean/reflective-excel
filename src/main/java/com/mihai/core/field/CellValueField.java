package com.mihai.core.field;

import java.lang.reflect.Field;

import org.apache.poi.ss.util.CellReference;

public class CellValueField implements AnnotatedField {

    private final String cellValueReference;
    private final Field field;

    public CellValueField(String cellValueReference, Field field) {
        this.cellValueReference = cellValueReference;
        this.field = field;
    }

    public String getCellValueReference() {
        return cellValueReference;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AnnotatedFieldType getType() {
        return AnnotatedFieldType.CELL_VALUE;
    }

    public int getRow() {
        return new CellReference(cellValueReference).getRow();
    }

    public int getColumn() {
        return new CellReference(cellValueReference).getCol();
    }
}
