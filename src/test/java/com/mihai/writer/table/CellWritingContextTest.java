package com.mihai.writer.table;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellWritingContextTest {

    @Test
    public void settingRowSavesValue() {
        CellWritingContext context = new CellWritingContext();
        context.setCurrentRow(42);

        assertEquals(42, context.getCurrentRow());
    }

    @Test
    public void settingColumnSavesValue() {
        CellWritingContext context = new CellWritingContext();
        context.setCurrentColumn(42);

        assertEquals(42, context.getCurrentColumn());
    }

    @Test
    public void resettingDefaultsToNegativeRowAndColumn() {
        CellWritingContext context = new CellWritingContext();
        context.setCurrentRow(42);
        context.setCurrentColumn(10);
        context.reset();

        assertEquals(-1, context.getCurrentRow());
        assertEquals(-1, context.getCurrentColumn());
    }
}
