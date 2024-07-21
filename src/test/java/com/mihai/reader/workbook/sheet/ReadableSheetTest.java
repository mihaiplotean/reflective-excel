package com.mihai.reader.workbook.sheet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.mihai.reader.ExcelReadingTest;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Test;

public class ReadableSheetTest extends ExcelReadingTest {

    @Test
    public void rowContainsOnlyCellsWithDefinedValue() {
        Row row = createRow(0);
        row.createCell(1).setCellValue("A");
        row.createCell(2).setCellValue("B");

        List<ReadableCell> cells = getReadableSheet().getRow(0).getCells();
        assertEquals(2, cells.size());
        assertEquals("A", cells.get(0).getValue());
        assertEquals("B", cells.get(1).getValue());
    }

    @Test
    public void mergedCellsAreReadAsOneCell() {
        Row row1 = createRow(0);
        row1.createCell(4).setCellValue("Merged Cell C");
        mergeRegion(0, 2, 4, 5);

        Row row2 = createRow(1);
        row2.createCell(1).setCellValue("One Cell A");
        row2.createCell(2).setCellValue("One Cell B");

        List<ReadableCell> cells = getReadableSheet().getRow(1).getCells();
        assertEquals(3, cells.size());
        assertEquals("One Cell A", cells.get(0).getValue());
        assertEquals("One Cell B", cells.get(1).getValue());
        assertEquals("Merged Cell C", cells.get(2).getValue());
    }

    @Test
    public void eachRowContainsTheSpannedMergedCell() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("Cell spanning multiple rows");
        mergeRegion(0, 2, 0, 2);

        ReadableSheet sheet = getReadableSheet();
        assertEquals(1, sheet.getRow(0).getCells().size());
        assertEquals(1, sheet.getRow(1).getCells().size());
        assertEquals(1, sheet.getRow(2).getCells().size());
    }

    @Test
    public void retrievingMergedCellByRowAndColumnReturnsTheSameCell() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("Merged Cell");
        mergeRegion(0, 1, 0, 1);

        ReadableSheet sheet = getReadableSheet();
        assertEquals("Merged Cell", sheet.getCell(0, 0).getValue());
        assertEquals("Merged Cell", sheet.getCell(0, 1).getValue());
        assertEquals("Merged Cell", sheet.getCell(1, 0).getValue());
        assertEquals("Merged Cell", sheet.getCell(1, 1).getValue());
    }
}
