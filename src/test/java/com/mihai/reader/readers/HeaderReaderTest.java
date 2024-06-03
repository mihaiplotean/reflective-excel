package com.mihai.reader.readers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mihai.reader.ExcelReadingSettings;
import com.mihai.reader.ExcelReadingTest;
import com.mihai.reader.ReadableSheetContext;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.detector.SimpleRowColumnDetector;
import com.mihai.reader.table.TableHeaders;
import com.mihai.reader.workbook.sheet.ReadableCell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Test;

class HeaderReaderTest extends ExcelReadingTest {

    @Test
    public void allHeadersInOneRowAreReadCorrectly() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("header A");
        row.createCell(1).setCellValue("header B");
        row.createCell(2).setCellValue("header C");

        ReadableSheetContext sheetContext = createSheetContext();

        HeaderReader headerReader = new HeaderReader(sheetContext, new SimpleRowColumnDetector("A1"));
        TableHeaders readHeaders = headerReader.readHeaders();

        assertEquals("header A", readHeaders.getHeader(0).getValue());
        assertEquals("header B", readHeaders.getHeader(1).getValue());
        assertEquals("header C", readHeaders.getHeader(2).getValue());
    }

    @Test
    public void nestedHeadersAreReadCorrectly() {
        Row firstRow = createRow(0);
        Row secondRow = createRow(1);

        firstRow.createCell(0).setCellValue("parent header");
        firstRow.createCell(2).setCellValue("header 2");
        secondRow.createCell(0).setCellValue("sub-header A");
        secondRow.createCell(1).setCellValue("sub-header B");
        mergeRegion(0, 0, 0, 1);
        mergeRegion(0, 1, 2, 2);

        ReadableSheetContext sheetContext = createSheetContext();
        HeaderReader headerReader = new HeaderReader(sheetContext, new SimpleRowColumnDetector("A1"));
        TableHeaders readHeaders = headerReader.readHeaders();

        assertEquals("sub-header A", readHeaders.getHeader(0).getValue());
        assertEquals("sub-header B", readHeaders.getHeader(1).getValue());
        assertEquals("header 2", readHeaders.getHeader(2).getValue());
    }

    @Test
    public void headerStartNotFoundReturnsEmptyHeader() {
        Row headerRow = createRow(0);
        headerRow.createCell(0).setCellValue("A");
        headerRow.createCell(1).setCellValue("B");

        SimpleRowColumnDetector rowColumnDetector = new SimpleRowColumnDetector("A1") {
            @Override
            public boolean isHeaderStartColumn(ReadingContext context, ReadableCell cell) {
                return false;
            }
        };
        ExcelReadingSettings settings = ExcelReadingSettings.builder()
                .rowColumnDetector(rowColumnDetector)
                .build();
        HeaderReader headerReader = new HeaderReader(createSheetContext(settings), rowColumnDetector);
        TableHeaders readHeaders = headerReader.readHeaders();

        assertTrue(readHeaders.getCells().isEmpty());
    }

    @Test
    public void headerEndNotFoundReturnsEmptyHeader() {
        Row headerRow = createRow(0);
        headerRow.createCell(0).setCellValue("A");
        headerRow.createCell(1).setCellValue("B");

        SimpleRowColumnDetector rowColumnDetector = new SimpleRowColumnDetector("A1") {
            @Override
            public boolean isHeaderLastColumn(ReadingContext context, ReadableCell cell) {
                return false;
            }
        };
        ExcelReadingSettings settings = ExcelReadingSettings.builder()
                .rowColumnDetector(rowColumnDetector)
                .build();
        HeaderReader headerReader = new HeaderReader(createSheetContext(settings), rowColumnDetector);
        TableHeaders readHeaders = headerReader.readHeaders();

        assertTrue(readHeaders.getCells().isEmpty());
    }
}
