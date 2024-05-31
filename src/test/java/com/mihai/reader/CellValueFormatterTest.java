package com.mihai.reader;

import org.apache.poi.ss.usermodel.Cell;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CellValueFormatterTest extends ExcelReadingTest {

    @Test
    public void intValueCorrectlyFormatted() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue(42);
        assertEquals("42", new CellValueFormatter(getWorkbook()).toString(cell));
    }

    @Test
    public void doubleValueCorrectlyFormatted() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue(42.2);
        assertEquals("42.2", new CellValueFormatter(getWorkbook()).toString(cell));
    }

    @Test
    public void booleanValueCorrectlyFormatted() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue(true);
        assertEquals("TRUE", new CellValueFormatter(getWorkbook()).toString(cell));
    }
}
