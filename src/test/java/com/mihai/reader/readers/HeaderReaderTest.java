package com.mihai.reader.readers;

import com.mihai.reader.ReadableSheetContext;
import com.mihai.reader.deserializer.DefaultDeserializationContext;
import com.mihai.reader.detector.SimpleRowColumnDetector;
import com.mihai.reader.table.TableHeaders;
import com.mihai.reader.workbook.sheet.ReadableSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeaderReaderTest {

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
    public void allHeadersInOneRowAreReadCorrectly() {
        Row row = actualSheet.createRow(0);
        row.createCell(0).setCellValue("header A");
        row.createCell(1).setCellValue("header B");
        row.createCell(2).setCellValue("header C");

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, new DefaultDeserializationContext(), null);

        HeaderReader headerReader = new HeaderReader(sheetContext, new SimpleRowColumnDetector("A1"));
        TableHeaders readHeaders = headerReader.readHeaders();

        assertEquals("header A", readHeaders.getHeader(0).getValue());
        assertEquals("header B", readHeaders.getHeader(1).getValue());
        assertEquals("header C", readHeaders.getHeader(2).getValue());
    }

    @Test
    public void nestedHeadersAreReadCorrectly() {
        Row firstRow = actualSheet.createRow(0);
        Row secondRow = actualSheet.createRow(1);

        firstRow.createCell(0).setCellValue("parent header");
        firstRow.createCell(2).setCellValue("header 2");
        secondRow.createCell(0).setCellValue("sub-header A");
        secondRow.createCell(1).setCellValue("sub-header B");
        actualSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
        actualSheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));

        ReadableSheetContext sheetContext = new ReadableSheetContext(sheet, new DefaultDeserializationContext(), null);
        HeaderReader headerReader = new HeaderReader(sheetContext, new SimpleRowColumnDetector("A1"));
        TableHeaders readHeaders = headerReader.readHeaders();

        assertEquals("sub-header A", readHeaders.getHeader(0).getValue());
        assertEquals("sub-header B", readHeaders.getHeader(1).getValue());
        assertEquals("header 2", readHeaders.getHeader(2).getValue());
    }
}
