package com.mihai.reader;

import com.mihai.reader.workbook.sheet.ReadableSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CellValueFormatterTest {

    private XSSFWorkbook workbook;
    private XSSFSheet actualSheet;
    private ReadableSheet sheet;

    @BeforeEach
    public void setUp() {
        workbook = new XSSFWorkbook();
        actualSheet = workbook.createSheet();
        sheet = new ReadableSheet(actualSheet);
    }

    @AfterEach
    public void tearDown() throws IOException {
        workbook.close();
    }

    @Test
    public void intValueCorrectlyFormatted() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue(42);
        assertEquals("42", new CellValueFormatter(workbook).toString(cell));
    }

    @Test
    public void doubleValueCorrectlyFormatted() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue(42.2);
        assertEquals("42.2", new CellValueFormatter(workbook).toString(cell));
    }

    @Test
    public void booleanValueCorrectlyFormatted() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue(true);
        assertEquals("TRUE", new CellValueFormatter(workbook).toString(cell));
    }
}
