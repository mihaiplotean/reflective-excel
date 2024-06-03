package com.mihai.reader;

import java.io.IOException;

import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.workbook.sheet.ReadableSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class ExcelReadingTest {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private ReadableSheet readableSheet;

    @BeforeEach
    public void setUp() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet();
        readableSheet = new ReadableSheet(sheet);
    }

    @AfterEach
    public void tearDown() throws IOException {
        workbook.close();
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public Row createRow(int row) {
        return sheet.createRow(row);
    }

    public Sheet getSheet() {
        return sheet;
    }

    public ReadableSheet getReadableSheet() {
        return readableSheet;
    }

    public ReadableCell getReadableCell(int row, int column) {
        return readableSheet.getCell(row, column);
    }

    public ReadableSheetContext createSheetContext() {
        return createSheetContext(ExcelReadingSettings.DEFAULT);
    }

    public ReadableSheetContext createSheetContext(ExcelReadingSettings settings) {
        return new ReadableSheetContext(readableSheet, settings);
    }

    public void mergeRegion(int startRow, int endRow, int startColumn, int endColumn) {
        sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startColumn, endColumn));
    }
}
