package com.mihai.reader.workbook.sheet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.mihai.core.workbook.Bounds;
import com.mihai.reader.ExcelReadingTest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Test;

class MergedCellTest extends ExcelReadingTest {

    @Test
    public void cellsAreEqualIfPOICellIsEqual() {
        Cell cell = createRow(0).createCell(0);
        assertEquals(new MergedCell(cell, "a", new Bounds(0, 0, 0, 0)),
                     new MergedCell(cell, "b", new Bounds(0, 0, 0, 0)));
    }

    @Test
    public void equalsSameObject() {
        Cell cell = createRow(0).createCell(0);
        MergedCell mergedCell = new MergedCell(cell, "value", new Bounds(1, 1, 1, 1));
        assertEquals(mergedCell, mergedCell);
    }

    @Test
    public void doesNotEqualNull() {
        Cell cell = createRow(0).createCell(0);
        MergedCell mergedCell = new MergedCell(cell, "value", new Bounds(1, 1, 1, 1));
        assertNotEquals(mergedCell, null);
    }

    @Test
    public void doesNotEqualDifferentClass() {
        Cell cell = createRow(0).createCell(0);
        MergedCell mergedCell = new MergedCell(cell, "value", new Bounds(1, 1, 1, 1));
        assertNotEquals(mergedCell, new Object());
    }

    @Test
    public void doesNotEqualIfPOICellIsDifferent() {
        Row row = createRow(0);
        Cell cellA = row.createCell(0);
        MergedCell mergedCellA = new MergedCell(cellA, "value", new Bounds(1, 1, 1, 1));

        Cell cellB = row.createCell(1);
        MergedCell mergedCellB = new MergedCell(cellB, "value", new Bounds(1, 1, 1, 1));

        assertNotEquals(mergedCellA, mergedCellB);
    }

    @Test
    public void doesNotEqualIfBoundsAreDifferent() {
        Cell cell = createRow(0).createCell(0);
        MergedCell mergedCellA = new MergedCell(cell, "value", new Bounds(1, 1, 1, 1));
        MergedCell mergedCellB = new MergedCell(cell, "value", new Bounds(0, 0, 0, 0));

        assertNotEquals(mergedCellA, mergedCellB);
    }
}
