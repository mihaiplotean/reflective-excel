package com.reflectiveexcel.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.jupiter.api.Test;

public class WritableSheetTest extends ExcelWritingTest {

    @Test
    public void cellIsCreatedOnWrite() {
        Cell writtenCell = getWritableSheet().writeCell(new WritableCell("test", 1, 2));

        assertEquals(1, writtenCell.getRowIndex());
        assertEquals(2, writtenCell.getColumnIndex());
        assertEquals("test", writtenCell.getStringCellValue());
    }

    @Test
    public void cellRegionIsCreatedOnWritingACellSpawningMultipleRowsAndColumns() {
        WritableSheet sheet = getWritableSheet();

        WritableCell cell = new WritableCell("test", 0, 0, 2, 3);
        Cell writtenCell = sheet.writeCell(cell);
        sheet.mergeCellBounds(writtenCell, cell);

        CellRangeAddress mergedRegion = getSheet().getMergedRegion(0);
        assertEquals(0, mergedRegion.getFirstRow());
        assertEquals(0, mergedRegion.getFirstColumn());
        assertEquals(2, mergedRegion.getLastRow());
        assertEquals(3, mergedRegion.getLastColumn());
    }
}
