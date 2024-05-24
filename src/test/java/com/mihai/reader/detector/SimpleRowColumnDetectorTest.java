package com.mihai.reader.detector;

import com.mihai.reader.ExcelReadingSettings;
import com.mihai.reader.ReadableSheetContext;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.deserializer.DefaultDeserializationContext;
import com.mihai.reader.workbook.sheet.ReadableSheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleRowColumnDetectorTest {

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
    public void headerRowIsSuppliedCellRow() {
        actualSheet.createRow(0).createCell(0).setCellValue("header");
        actualSheet.createRow(1).createCell(0).setCellValue("header 2");

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        ReadingContext readingContext = sheetContext.getReadingContext();

        SimpleRowColumnDetector rowColumnDetector = new SimpleRowColumnDetector("C2");
        assertFalse(rowColumnDetector.isHeaderRow(readingContext, readingContext.getRow(0)));
        assertTrue(rowColumnDetector.isHeaderRow(readingContext, readingContext.getRow(1)));
    }

    @Test
    public void headerStartColumnIsSuppliedCellColumn() {
        actualSheet.createRow(0).createCell(0).setCellValue("header");
        actualSheet.getRow(0).createCell(1).setCellValue("header 2");

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        ReadingContext readingContext = sheetContext.getReadingContext();

        SimpleRowColumnDetector rowColumnDetector = new SimpleRowColumnDetector("B2");
        assertFalse(rowColumnDetector.isHeaderStartColumn(readingContext, readingContext.getRow(0).getCell(0)));
        assertTrue(rowColumnDetector.isHeaderStartColumn(readingContext, readingContext.getRow(0).getCell(1)));
    }

    @Test
    public void headerLastColumnIsBeforeEmpty() {
        actualSheet.createRow(0).createCell(0).setCellValue("header");
        actualSheet.getRow(0).createCell(1).setCellValue("non-header");

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        ReadingContext readingContext = sheetContext.getReadingContext();

        assertTrue(new SimpleRowColumnDetector("C2").isHeaderLastColumn(readingContext, readingContext.getRow(0).getCell(1)));
    }

    @Test
    public void lastRowIsBeforeEmptyRow() {
        actualSheet.createRow(0).createCell(0).setCellValue("table header");
        actualSheet.createRow(1).createCell(0).setCellValue("row 1");
        actualSheet.createRow(2).createCell(0).setCellValue("row 2");
        actualSheet.createRow(3).createCell(0).setCellValue("");  // no value in row 3

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        ReadingContext readingContext = sheetContext.getReadingContext();

        SimpleRowColumnDetector rowColumnDetector = new SimpleRowColumnDetector("C2");
        assertFalse(rowColumnDetector.isLastRow(readingContext, readingContext.getRow(1)));
        assertTrue(rowColumnDetector.isLastRow(readingContext, readingContext.getRow(2)));
    }
}