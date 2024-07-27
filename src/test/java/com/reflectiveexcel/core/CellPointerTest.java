package com.reflectiveexcel.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CellPointerTest {

    @Test
    public void settingRowSavesValue() {
        CellPointer context = new CellPointer();
        context.setCurrentRow(42);

        assertEquals(42, context.getCurrentRow());
    }

    @Test
    public void settingColumnSavesValue() {
        CellPointer context = new CellPointer();
        context.setCurrentColumn(42);

        assertEquals(42, context.getCurrentColumn());
    }

    @Test
    public void resettingDefaultsToNegativeRowAndColumn() {
        CellPointer context = new CellPointer();
        context.setCurrentRow(42);
        context.setCurrentColumn(10);
        context.reset();

        assertEquals(-1, context.getCurrentRow());
        assertEquals(-1, context.getCurrentColumn());
    }
}
