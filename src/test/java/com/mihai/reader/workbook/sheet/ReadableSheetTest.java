package com.mihai.reader.workbook.sheet;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
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

    @Test
    public void mergedCellsAreReadAsOneCell() {
        Row row1 = actualSheet.createRow(0);
        row1.createCell(4).setCellValue("Merged Cell C");
        actualSheet.addMergedRegion(new CellRangeAddress(0, 2, 4, 5));

        Row row2 = actualSheet.createRow(1);
        row2.createCell(1).setCellValue("One Cell A");
        row2.createCell(2).setCellValue("One Cell B");

        List<ReadableCell> cells = sheet.getRow(1).getCells();
        assertEquals(3, cells.size());
        assertEquals("One Cell A", cells.get(0).getValue());
        assertEquals("One Cell B", cells.get(1).getValue());
        assertEquals("Merged Cell C", cells.get(2).getValue());
    }

    @Test
    public void eachRowContainsTheSpannedMergedCell() {
        Row row = actualSheet.createRow(0);
        row.createCell(0).setCellValue("Cell spanning multiple rows");
        actualSheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 2));

        assertEquals(1, sheet.getRow(0).getCells().size());
        assertEquals(1, sheet.getRow(1).getCells().size());
        assertEquals(1, sheet.getRow(2).getCells().size());
    }

    @Test
    public void retrievingMergedCellByRowAndColumnReturnsTheSameCell() {
        Row row = actualSheet.createRow(0);
        row.createCell(0).setCellValue("Merged Cell");
        actualSheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 1));

        assertEquals("Merged Cell", sheet.getCell(0, 0).getValue());
        assertEquals("Merged Cell", sheet.getCell(0, 1).getValue());
        assertEquals("Merged Cell", sheet.getCell(1, 0).getValue());
        assertEquals("Merged Cell", sheet.getCell(1, 1).getValue());
    }
}