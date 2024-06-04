package com.mihai.reader.detector;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.mihai.core.annotation.ExcelColumn;
import com.mihai.reader.ExcelReadingTest;
import com.mihai.reader.ReadableSheetContext;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.bean.RootTableBeanReadNode;
import com.mihai.reader.table.TableHeader;
import com.mihai.reader.table.TableHeaders;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Test;

public class AutoRowColumnDetectorTest extends ExcelReadingTest {

    @Test
    public void headerRowIsFoundBasedOnDefinedColumnsInBean() {
        createRow(0).createCell(0).setCellValue("header");

        ReadableSheetContext sheetContext = createSheetContext();
        sheetContext.setCurrentTableBean(new RootTableBeanReadNode(OneColumnRow.class));
        sheetContext.setReadingTable(true);

        ReadingContext readingContext = sheetContext.getReadingContext();
        assertTrue(new AutoRowColumnDetector().isHeaderRow(readingContext, readingContext.getRow(0)));
    }

    @Test
    public void headerStartColumnIsFoundBasedOnDefinedColumnsInBean() {
        createRow(0).createCell(0).setCellValue("non-header");
        createRow(1).createCell(0).setCellValue("header");

        ReadableSheetContext sheetContext = createSheetContext();
        sheetContext.setCurrentTableBean(new RootTableBeanReadNode(OneColumnRow.class));
        sheetContext.setReadingTable(true);

        ReadingContext readingContext = sheetContext.getReadingContext();
        AutoRowColumnDetector rowColumnDetector = new AutoRowColumnDetector();
        assertFalse(rowColumnDetector.isHeaderStartColumn(readingContext, readingContext.getRow(0).getCell(0)));
        assertTrue(rowColumnDetector.isHeaderStartColumn(readingContext, readingContext.getRow(1).getCell(0)));
    }

    @Test
    public void headerLastColumnIsBeforeEmpty() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("non-header");
        row.createCell(1).setCellValue("header");
        row.createCell(2).setCellValue("");  // no value in column C

        ReadableSheetContext sheetContext = createSheetContext();
        sheetContext.setCurrentTableBean(new RootTableBeanReadNode(OneColumnRow.class));
        sheetContext.setReadingTable(true);

        ReadingContext readingContext = sheetContext.getReadingContext();
        assertTrue(new AutoRowColumnDetector().isHeaderLastColumn(readingContext, readingContext.getRow(0).getCell(1)));
    }

    @Test
    public void lastRowIsBeforeEmptyRow() {
        createRow(0).createCell(0).setCellValue("table header");
        createRow(1).createCell(0).setCellValue("row 1");
        createRow(2).createCell(0).setCellValue("row 2");
        createRow(3).createCell(0).setCellValue("");  // no value in row 3

        ReadableSheetContext sheetContext = createSheetContext();
        sheetContext.setCurrentTableHeaders(new TableHeaders(List.of(new TableHeader(getReadableCell(0, 0)))));
        sheetContext.setReadingTable(true);

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
