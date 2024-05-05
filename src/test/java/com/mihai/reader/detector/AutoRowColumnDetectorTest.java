package com.mihai.reader.detector;

import com.mihai.reader.ReadableSheetContext;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.deserializer.DefaultDeserializationContext;
import com.mihai.reader.table.TableHeader;
import com.mihai.reader.table.TableHeaders;
import com.mihai.reader.table.TableReadingContext;
import com.mihai.reader.workbook.sheet.ReadableRow;
import com.mihai.reader.workbook.sheet.ReadableSheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        actualSheet.createRow(0).createCell(0).setCellValue("table header");
        actualSheet.createRow(1).createCell(0).setCellValue("row 1");
        actualSheet.createRow(2).createCell(0).setCellValue("row 2");
        actualSheet.createRow(3).createCell(0).setCellValue("");  // no value in row 3

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, new DefaultDeserializationContext(), null);
        sheetContext.setCurrentTableHeaders(new TableHeaders(List.of(new TableHeader(sheet.getCell(0, 0)))));

        ReadingContext readingContext = sheetContext.getReadingContext();
        AutoRowColumnDetector rowColumnDetector = new AutoRowColumnDetector();
        assertFalse(rowColumnDetector.isLastRow(readingContext, readingContext.getRow(1)));
        assertTrue(rowColumnDetector.isLastRow(readingContext, readingContext.getRow(2)));
    }

    @Test
    public void lastRowIsBeforeEmptyRow() {
        actualSheet.createRow(0).createCell(0).setCellValue("table header");
        actualSheet.createRow(1).createCell(0).setCellValue("row 1");
        actualSheet.createRow(2).createCell(0).setCellValue("row 2");
        actualSheet.createRow(3).createCell(0).setCellValue("");  // no value in row 3

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, new DefaultDeserializationContext(), null);
        sheetContext.setCurrentTableHeaders(new TableHeaders(List.of(new TableHeader(sheet.getCell(0, 0)))));

        ReadingContext readingContext = sheetContext.getReadingContext();
        AutoRowColumnDetector rowColumnDetector = new AutoRowColumnDetector();
        assertFalse(rowColumnDetector.isLastRow(readingContext, readingContext.getRow(1)));
        assertTrue(rowColumnDetector.isLastRow(readingContext, readingContext.getRow(2)));
    }
}
