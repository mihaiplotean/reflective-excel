package com.mihai.reader.detector;

import com.mihai.common.annotation.ExcelColumn;
import com.mihai.reader.ExcelReadingSettings;
import com.mihai.reader.ReadableSheetContext;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.bean.RootTableBeanReadNode;
import com.mihai.reader.deserializer.DefaultDeserializationContext;
import com.mihai.reader.table.TableHeader;
import com.mihai.reader.table.TableHeaders;
import com.mihai.reader.workbook.sheet.ReadableSheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AutoRowColumnDetectorTest {

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
    public void headerRowIsFoundBasedOnDefinedColumnsInBean() {
        actualSheet.createRow(0).createCell(0).setCellValue("header");

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        sheetContext.setCurrentTableBean(new RootTableBeanReadNode(OneColumnRow.class));

        ReadingContext readingContext = sheetContext.getReadingContext();
        assertTrue(new AutoRowColumnDetector().isHeaderRow(readingContext, readingContext.getRow(0)));
    }

    @Test
    public void headerStartColumnIsFoundBasedOnDefinedColumnsInBean() {
        actualSheet.createRow(0).createCell(0).setCellValue("non-header");
        actualSheet.createRow(1).createCell(0).setCellValue("header");

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        sheetContext.setCurrentTableBean(new RootTableBeanReadNode(OneColumnRow.class));

        ReadingContext readingContext = sheetContext.getReadingContext();
        AutoRowColumnDetector rowColumnDetector = new AutoRowColumnDetector();
        assertFalse(rowColumnDetector.isHeaderStartColumn(readingContext, readingContext.getRow(0).getCell(0)));
        assertTrue(rowColumnDetector.isHeaderStartColumn(readingContext, readingContext.getRow(1).getCell(0)));
    }

    @Test
    public void headerLastColumnIsBeforeEmpty() {
        actualSheet.createRow(0).createCell(0).setCellValue("non-header");
        actualSheet.getRow(0).createCell(1).setCellValue("header");
        actualSheet.getRow(0).createCell(2).setCellValue("");  // no value in column C

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        sheetContext.setCurrentTableBean(new RootTableBeanReadNode(OneColumnRow.class));

        ReadingContext readingContext = sheetContext.getReadingContext();
        assertTrue(new AutoRowColumnDetector().isHeaderLastColumn(readingContext, readingContext.getRow(0).getCell(1)));
    }

    @Test
    public void lastRowIsBeforeEmptyRow() {
        actualSheet.createRow(0).createCell(0).setCellValue("table header");
        actualSheet.createRow(1).createCell(0).setCellValue("row 1");
        actualSheet.createRow(2).createCell(0).setCellValue("row 2");
        actualSheet.createRow(3).createCell(0).setCellValue("");  // no value in row 3

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        sheetContext.setCurrentTableHeaders(new TableHeaders(List.of(new TableHeader(sheet.getCell(0, 0)))));

        ReadingContext readingContext = sheetContext.getReadingContext();
        AutoRowColumnDetector rowColumnDetector = new AutoRowColumnDetector();
        assertFalse(rowColumnDetector.isLastRow(readingContext, readingContext.getRow(1)));
        assertTrue(rowColumnDetector.isLastRow(readingContext, readingContext.getRow(2)));
    }

    private static class OneColumnRow {

        @ExcelColumn("header")
        private String header;
    }
}
