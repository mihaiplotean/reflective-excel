package com.mihai.reader.detector;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mihai.reader.ExcelReadingTest;
import com.mihai.reader.ReadableSheetContext;
import com.mihai.reader.ReadingContext;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Test;

class MaybeDynamicColumnTest extends ExcelReadingTest {

    @Test
    public void leftCellIsNotAfterRightCell() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("A");
        row.createCell(1).setCellValue("B");

        ReadableSheetContext sheetContext = createSheetContext();
        sheetContext.setCurrentRow(0);
        ReadingContext readingContext = sheetContext.getReadingContext();
        MaybeDynamicColumn maybeDynamicColumn = new MaybeDynamicColumn(readingContext, readingContext.getRow(0).getCell(0));

        assertFalse(maybeDynamicColumn.isAfter("B"));
    }

    @Test
    public void rightCellIsAfterLeftCell() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("A");
        row.createCell(1).setCellValue("B");

        ReadableSheetContext sheetContext = createSheetContext();
        sheetContext.setCurrentRow(0);
        ReadingContext readingContext = sheetContext.getReadingContext();
        MaybeDynamicColumn maybeDynamicColumn = new MaybeDynamicColumn(readingContext, readingContext.getRow(0).getCell(1));

        assertTrue(maybeDynamicColumn.isAfter("A"));
    }

    @Test
    public void cellIsNotAfterNonExistingCellValue() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("A");

        ReadableSheetContext sheetContext = createSheetContext();
        sheetContext.setCurrentRow(0);
        ReadingContext readingContext = sheetContext.getReadingContext();
        MaybeDynamicColumn maybeDynamicColumn = new MaybeDynamicColumn(readingContext, readingContext.getRow(0).getCell(0));

        assertFalse(maybeDynamicColumn.isAfter("C"));
    }

    @Test
    public void rightCellIsNotBeforeLeftCell() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("A");
        row.createCell(1).setCellValue("B");

        ReadableSheetContext sheetContext = createSheetContext();
        sheetContext.setCurrentRow(0);
        ReadingContext readingContext = sheetContext.getReadingContext();
        MaybeDynamicColumn maybeDynamicColumn = new MaybeDynamicColumn(readingContext, readingContext.getRow(0).getCell(1));

        assertFalse(maybeDynamicColumn.isBefore("A"));
    }

    @Test
    public void leftCellIsBeforeRightCell() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("A");
        row.createCell(1).setCellValue("B");

        ReadableSheetContext sheetContext = createSheetContext();
        sheetContext.setCurrentRow(0);
        ReadingContext readingContext = sheetContext.getReadingContext();
        MaybeDynamicColumn maybeDynamicColumn = new MaybeDynamicColumn(readingContext, readingContext.getRow(0).getCell(0));

        assertTrue(maybeDynamicColumn.isBefore("B"));
    }

    @Test
    public void cellIsNotBeforeNonExistingCellValue() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("A");

        ReadableSheetContext sheetContext = createSheetContext();
        sheetContext.setCurrentRow(0);
        ReadingContext readingContext = sheetContext.getReadingContext();
        MaybeDynamicColumn maybeDynamicColumn = new MaybeDynamicColumn(readingContext, readingContext.getRow(0).getCell(0));

        assertFalse(maybeDynamicColumn.isBefore("C"));
    }

    @Test
    public void middleCellIsBetweenLeftAndRightCell() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("A");
        row.createCell(1).setCellValue("B");
        row.createCell(2).setCellValue("C");

        ReadableSheetContext sheetContext = createSheetContext();
        sheetContext.setCurrentRow(0);
        ReadingContext readingContext = sheetContext.getReadingContext();
        MaybeDynamicColumn maybeDynamicColumn = new MaybeDynamicColumn(readingContext, readingContext.getRow(0).getCell(1));

        assertTrue(maybeDynamicColumn.isBetween("A", "C"));
    }

    @Test
    public void secondCellIsNotBetweenFirstAndItself() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("A");
        row.createCell(1).setCellValue("B");

        ReadableSheetContext sheetContext = createSheetContext();
        sheetContext.setCurrentRow(0);
        ReadingContext readingContext = sheetContext.getReadingContext();
        MaybeDynamicColumn maybeDynamicColumn = new MaybeDynamicColumn(readingContext, readingContext.getRow(0).getCell(1));

        assertFalse(maybeDynamicColumn.isBetween("A", "B"));
    }
}
