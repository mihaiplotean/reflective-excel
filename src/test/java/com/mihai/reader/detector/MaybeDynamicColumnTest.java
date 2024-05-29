package com.mihai.reader.detector;

import com.mihai.reader.ExcelReadingSettings;
import com.mihai.reader.ReadableSheetContext;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.workbook.sheet.ReadableSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MaybeDynamicColumnTest {

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
    public void leftCellIsNotAfterRightCell() {
        Row row = actualSheet.createRow(0);
        row.createCell(0).setCellValue("A");
        row.createCell(1).setCellValue("B");

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        sheetContext.setCurrentRow(0);
        ReadingContext readingContext = sheetContext.getReadingContext();
        MaybeDynamicColumn maybeDynamicColumn = new MaybeDynamicColumn(readingContext, readingContext.getRow(0).getCell(0));

        assertFalse(maybeDynamicColumn.isAfter("B"));
    }

    @Test
    public void rightCellIsAfterLeftCell() {
        Row row = actualSheet.createRow(0);
        row.createCell(0).setCellValue("A");
        row.createCell(1).setCellValue("B");

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        sheetContext.setCurrentRow(0);
        ReadingContext readingContext = sheetContext.getReadingContext();
        MaybeDynamicColumn maybeDynamicColumn = new MaybeDynamicColumn(readingContext, readingContext.getRow(0).getCell(1));

        assertTrue(maybeDynamicColumn.isAfter("A"));
    }

    @Test
    public void cellIsNotAfterNonExistingCellValue() {
        Row row = actualSheet.createRow(0);
        row.createCell(0).setCellValue("A");

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        sheetContext.setCurrentRow(0);
        ReadingContext readingContext = sheetContext.getReadingContext();
        MaybeDynamicColumn maybeDynamicColumn = new MaybeDynamicColumn(readingContext, readingContext.getRow(0).getCell(0));

        assertFalse(maybeDynamicColumn.isAfter("C"));
    }

    @Test
    public void rightCellIsNotBeforeLeftCell() {
        Row row = actualSheet.createRow(0);
        row.createCell(0).setCellValue("A");
        row.createCell(1).setCellValue("B");

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        sheetContext.setCurrentRow(0);
        ReadingContext readingContext = sheetContext.getReadingContext();
        MaybeDynamicColumn maybeDynamicColumn = new MaybeDynamicColumn(readingContext, readingContext.getRow(0).getCell(1));

        assertFalse(maybeDynamicColumn.isBefore("A"));
    }

    @Test
    public void leftCellIsBeforeRightCell() {
        Row row = actualSheet.createRow(0);
        row.createCell(0).setCellValue("A");
        row.createCell(1).setCellValue("B");

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        sheetContext.setCurrentRow(0);
        ReadingContext readingContext = sheetContext.getReadingContext();
        MaybeDynamicColumn maybeDynamicColumn = new MaybeDynamicColumn(readingContext, readingContext.getRow(0).getCell(0));

        assertTrue(maybeDynamicColumn.isBefore("B"));
    }

    @Test
    public void cellIsNotBeforeNonExistingCellValue() {
        Row row = actualSheet.createRow(0);
        row.createCell(0).setCellValue("A");

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        sheetContext.setCurrentRow(0);
        ReadingContext readingContext = sheetContext.getReadingContext();
        MaybeDynamicColumn maybeDynamicColumn = new MaybeDynamicColumn(readingContext, readingContext.getRow(0).getCell(0));

        assertFalse(maybeDynamicColumn.isBefore("C"));
    }

    @Test
    public void middleCellIsBetweenLeftAndRightCell() {
        Row row = actualSheet.createRow(0);
        row.createCell(0).setCellValue("A");
        row.createCell(1).setCellValue("B");
        row.createCell(2).setCellValue("C");

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        sheetContext.setCurrentRow(0);
        ReadingContext readingContext = sheetContext.getReadingContext();
        MaybeDynamicColumn maybeDynamicColumn = new MaybeDynamicColumn(readingContext, readingContext.getRow(0).getCell(1));

        assertTrue(maybeDynamicColumn.isBetween("A", "C"));
    }

    @Test
    public void secondCellIsNotBetweenFirstAndItself() {
        Row row = actualSheet.createRow(0);
        row.createCell(0).setCellValue("A");
        row.createCell(1).setCellValue("B");

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        sheetContext.setCurrentRow(0);
        ReadingContext readingContext = sheetContext.getReadingContext();
        MaybeDynamicColumn maybeDynamicColumn = new MaybeDynamicColumn(readingContext, readingContext.getRow(0).getCell(1));

        assertFalse(maybeDynamicColumn.isBetween("A", "B"));
    }
}