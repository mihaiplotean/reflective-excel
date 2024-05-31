package com.mihai.reader.workbook.sheet;

import com.mihai.reader.ExcelReadingTest;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ReadableRowTest extends ExcelReadingTest {

    @Test
    public void rowsAreEqualIfCellsAndRowNumberIsEqual() {
        Cell cell = createRow(0).createCell(0);
        assertEquals(new ReadableRow(1, List.of(new SimpleCell(cell, "value"))),
                new ReadableRow(1, List.of(new SimpleCell(cell, "value"))));
    }

    @Test
    public void equalsSameObject() {
        ReadableRow row = new ReadableRow(1, List.of());
        assertEquals(row, row);
    }

    @Test
    public void doesNotEqualNull() {
        ReadableRow row = new ReadableRow(1, List.of());
        assertNotEquals(row, null);
    }

    @Test
    public void doesNotEqualDifferentClass() {
        ReadableRow row = new ReadableRow(1, List.of());
        assertNotEquals(row, new Object());
    }

    @Test
    public void doesNotEqualIfRowNumberIsDifferent() {
        ReadableRow rowA = new ReadableRow(1, List.of());
        ReadableRow rowB = new ReadableRow(2, List.of());

        assertNotEquals(rowA, rowB);
    }

    @Test
    public void doesNotEqualIfCellsAreDifferent() {
        Cell cellA = createRow(0).createCell(0);
        Cell cellB = createRow(1).createCell(1);

        ReadableRow rowA = new ReadableRow(1, List.of(new SimpleCell(cellA, "")));
        ReadableRow rowB = new ReadableRow(1, List.of(new SimpleCell(cellB, "")));

        assertNotEquals(rowA, rowB);
    }
}