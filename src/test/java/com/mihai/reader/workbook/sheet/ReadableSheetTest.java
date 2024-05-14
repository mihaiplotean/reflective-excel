package com.mihai.reader.workbook.sheet;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReadableSheetTest {

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
    public void rowContainsOnlyCellsWithDefinedValue() {
        Row row = actualSheet.createRow(0);
        row.createCell(1).setCellValue("A");
        row.createCell(2).setCellValue("B");

        List<ReadableCell> cells = sheet.getRow(0).getCells();
        assertEquals(2, cells.size());
        assertEquals("A", cells.get(0).getValue());
        assertEquals("B", cells.get(1).getValue());
    }
}