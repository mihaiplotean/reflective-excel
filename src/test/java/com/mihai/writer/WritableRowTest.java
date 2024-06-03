package com.mihai.writer;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Test;

class WritableRowTest extends ExcelWritingTest {

    @Test
    public void retrievingCellByColumnTwiceReturnsSameCell() {
        Row row = getSheet().createRow(0);
        WritableRow writableRow = new WritableRow(row);
        Cell cellA = writableRow.getOrCreateCell(0);
        Cell cellB = writableRow.getOrCreateCell(0);
        assertSame(cellA, cellB);
    }
}
