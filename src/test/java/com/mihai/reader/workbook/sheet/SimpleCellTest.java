package com.mihai.reader.workbook.sheet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.mihai.reader.ExcelReadingTest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Test;

public class SimpleCellTest extends ExcelReadingTest {

    @Test
    public void cellsAreEqualIfPOICellIsEqual() {
        Cell cell = createRow(0).createCell(0);
        assertEquals(new SimpleCell(cell, "a"), new SimpleCell(cell, "b"));
    }

    @Test
    public void equalsSameObject() {
        Cell cell = createRow(0).createCell(0);
        SimpleCell simpleCell = new SimpleCell(cell, "value");
        assertEquals(simpleCell, simpleCell);
    }

    @Test
    public void doesNotEqualNull() {
        Cell cell = createRow(0).createCell(0);
        SimpleCell simpleCell = new SimpleCell(cell, "value");
        assertNotEquals(simpleCell, null);
    }

    @Test
    public void doesNotEqualDifferentClass() {
        Cell cell = createRow(0).createCell(0);
        SimpleCell simpleCell = new SimpleCell(cell, "value");
        assertNotEquals(simpleCell, new Object());
    }

    @Test
    public void doesNotEqualIfPOICellIsDifferent() {
        Row row = createRow(0);
        Cell cellA = row.createCell(0);
        SimpleCell simpleCellA = new SimpleCell(cellA, "value");

        Cell cellB = row.createCell(1);
        SimpleCell simpleCellB = new SimpleCell(cellB, "value");

        assertNotEquals(simpleCellA, simpleCellB);
    }
}
