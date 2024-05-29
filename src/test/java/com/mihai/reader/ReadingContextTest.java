package com.mihai.reader;

import com.mihai.reader.exception.BadInputException;
import com.mihai.reader.exception.BadInputExceptionConsumer;
import com.mihai.reader.workbook.sheet.ReadableSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ReadingContextTest {

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
    public void retrievingNonExistentCellValueByRowColumnReturnsNull() {
        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        assertNull(sheetContext.getReadingContext().getCellValue(42, 42));
    }

    @Test
    public void retrievingNonExistentCellValueByReferenceReturnsNull() {
        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        assertNull(sheetContext.getReadingContext().getCellValue("B2"));
    }

    @Test
    public void retrievingNonExistentCellValueByReferenceAndTypeReturnsNull() {
        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        assertNull(sheetContext.getReadingContext().getCellValue("B2", ReadingContextTest.class));
    }

    @Test
    public void retrievingNonExistentCellValueByRowColumnAndTypeReturnsNull() {
        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        assertNull(sheetContext.getReadingContext().getCellValue(1, 1, ReadingContextTest.class));
    }

    @Test
    public void retrievingCellByRowColumnAndTypeReturnsDeserializedValue() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue(42d);
        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        assertEquals(42d, sheetContext.getReadingContext().getCellValue(0, 0, Double.class));
    }

    @Test
    public void exceptionThrownWhenInvalidCellTypeSpecified() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue("string value");
        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.DEFAULT);
        assertThrows(BadInputException.class, () -> sheetContext.getReadingContext().getCellValue(0, 0, Double.class));
    }

    @Test
    public void catchingExceptionReturnsNullWhenRetrievingCellValue() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue("string value");
        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, ExcelReadingSettings.builder()
                .onException((readingContext, exception) -> {
                    // do nothing
                })
                .build());
        assertNull(sheetContext.getReadingContext().getCellValue(0, 0, Double.class));
    }
}
