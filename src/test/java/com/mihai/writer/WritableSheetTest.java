package com.mihai.writer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WritableSheetTest {

    private XSSFWorkbook workbook;
    private XSSFSheet actualSheet;

    @BeforeEach
    public void setUp() {
        workbook = new XSSFWorkbook();
        actualSheet = workbook.createSheet();
    }

    @AfterEach
    public void tearDown() throws IOException {
        workbook.close();
    }

    @Test
    public void cellIsCreatedOnWrite() {
        WritableSheet sheet = new WritableSheet(actualSheet);
        Cell writtenCell = sheet.writeCell(new WritableCell("test", 1, 2));

        assertEquals(1, writtenCell.getRowIndex());
        assertEquals(2, writtenCell.getColumnIndex());
        assertEquals("test", writtenCell.getStringCellValue());
    }

    @Test
    public void cellRegionIsCreatedOnWritingACellSpawningMultipleRowsAndColumns() {
        WritableSheet sheet = new WritableSheet(actualSheet);

        WritableCell cell = new WritableCell("test", 0, 0, 2, 3);
        Cell writtenCell = sheet.writeCell(cell);
        sheet.mergeCellBounds(writtenCell, cell);

        CellRangeAddress mergedRegion = actualSheet.getMergedRegion(0);
        assertEquals(0, mergedRegion.getFirstRow());
        assertEquals(0, mergedRegion.getFirstColumn());
        assertEquals(2, mergedRegion.getLastRow());
        assertEquals(3, mergedRegion.getLastColumn());
    }
}
