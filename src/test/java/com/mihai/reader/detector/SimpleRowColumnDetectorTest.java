package com.mihai.reader.detector;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mihai.reader.ExcelReadingTest;
import com.mihai.reader.ReadableSheetContext;
import com.mihai.reader.ReadingContext;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Test;

class SimpleRowColumnDetectorTest extends ExcelReadingTest {

    @Test
    public void headerRowIsSuppliedCellRow() {
        createRow(0).createCell(0).setCellValue("header");
        createRow(1).createCell(0).setCellValue("header 2");

        ReadableSheetContext sheetContext = createSheetContext();
        ReadingContext readingContext = sheetContext.getReadingContext();

        SimpleRowColumnDetector rowColumnDetector = new SimpleRowColumnDetector("C2");
        assertFalse(rowColumnDetector.isHeaderRow(readingContext, readingContext.getRow(0)));
        assertTrue(rowColumnDetector.isHeaderRow(readingContext, readingContext.getRow(1)));
    }

    @Test
    public void headerStartColumnIsSuppliedCellColumn() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("header");
        row.createCell(1).setCellValue("header 2");

        ReadableSheetContext sheetContext = createSheetContext();
        ReadingContext readingContext = sheetContext.getReadingContext();

        SimpleRowColumnDetector rowColumnDetector = new SimpleRowColumnDetector("B2");
        assertFalse(rowColumnDetector.isHeaderStartColumn(readingContext, readingContext.getRow(0).getCell(0)));
        assertTrue(rowColumnDetector.isHeaderStartColumn(readingContext, readingContext.getRow(0).getCell(1)));
    }

    @Test
    public void headerLastColumnIsBeforeEmpty() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("header");
        row.createCell(1).setCellValue("non-header");

        ReadableSheetContext sheetContext = createSheetContext();
        ReadingContext readingContext = sheetContext.getReadingContext();

        assertTrue(new SimpleRowColumnDetector("C2").isHeaderLastColumn(readingContext, readingContext.getRow(0).getCell(1)));
    }

    @Test
    public void lastRowIsBeforeEmptyRow() {
        createRow(0).createCell(0).setCellValue("table header");
        createRow(1).createCell(0).setCellValue("row 1");
        createRow(2).createCell(0).setCellValue("row 2");
        createRow(3).createCell(0).setCellValue("");  // no value in row 3

        ReadableSheetContext sheetContext = createSheetContext();
        ReadingContext readingContext = sheetContext.getReadingContext();

        SimpleRowColumnDetector rowColumnDetector = new SimpleRowColumnDetector("C2");
        assertFalse(rowColumnDetector.isLastRow(readingContext, readingContext.getRow(1)));
        assertTrue(rowColumnDetector.isLastRow(readingContext, readingContext.getRow(2)));
    }
}
