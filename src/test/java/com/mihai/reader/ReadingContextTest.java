package com.mihai.reader;

import static org.junit.jupiter.api.Assertions.*;

import com.mihai.reader.exception.BadInputException;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.jupiter.api.Test;

class ReadingContextTest extends ExcelReadingTest {

    @Test
    public void retrievingNonExistentCellValueByRowColumnReturnsNull() {
        ReadableSheetContext sheetContext = createSheetContext();
        assertNull(sheetContext.getReadingContext().getCellValue(42, 42));
    }

    @Test
    public void retrievingNonExistentCellValueByReferenceReturnsNull() {
        ReadableSheetContext sheetContext = createSheetContext();
        assertNull(sheetContext.getReadingContext().getCellValue("B2"));
    }

    @Test
    public void retrievingNonExistentCellValueByReferenceAndTypeReturnsNull() {
        ReadableSheetContext sheetContext = createSheetContext();
        assertNull(sheetContext.getReadingContext().getCellValue("B2", ReadingContextTest.class));
    }

    @Test
    public void retrievingNonExistentCellValueByRowColumnAndTypeReturnsNull() {
        ReadableSheetContext sheetContext = createSheetContext();
        assertNull(sheetContext.getReadingContext().getCellValue(1, 1, ReadingContextTest.class));
    }

    @Test
    public void retrievingCellByRowColumnAndTypeReturnsDeserializedValue() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue(42d);
        ReadableSheetContext sheetContext = createSheetContext();
        assertEquals(42d, sheetContext.getReadingContext().getCellValue(0, 0, Double.class));
    }

    @Test
    public void exceptionThrownWhenInvalidCellTypeSpecified() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue("string value");
        ReadableSheetContext sheetContext = createSheetContext();
        assertThrows(BadInputException.class, () -> sheetContext.getReadingContext().getCellValue(0, 0, Double.class));
    }

    @Test
    public void catchingExceptionReturnsNullWhenRetrievingCellValue() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue("string value");
        ReadableSheetContext sheetContext = createSheetContext(ExcelReadingSettings.builder()
                                                                       .onException((readingContext, exception) -> {
                                                                           // do nothing
                                                                       })
                                                                       .build());
        assertNull(sheetContext.getReadingContext().getCellValue(0, 0, Double.class));
    }
}
