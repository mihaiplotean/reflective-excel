package com.reflectiveexcel.writer;

import java.io.IOException;

import com.reflectiveexcel.writer.serializer.DefaultSerializationContext;
import com.reflectiveexcel.writer.style.DefaultStyleContext;
import com.reflectiveexcel.writer.writers.CellWriter;
import com.reflectiveexcel.writer.writers.HeaderWriter;
import com.reflectiveexcel.writer.writers.TableWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class ExcelWritingTest {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private WritableSheet writableSheet;

    @BeforeEach
    public void setUp() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet();
        writableSheet = new WritableSheet(sheet);
    }

    @AfterEach
    public void tearDown() throws IOException {
        workbook.close();
    }

    public Sheet getSheet() {
        return sheet;
    }

    public WritableSheet getWritableSheet() {
        return writableSheet;
    }

    public Cell getCell(int row, int column) {
        return sheet.getRow(row).getCell(column);
    }

    public CellWriter createCellWriter() {
        return new CellWriter(writableSheet);
    }

    public HeaderWriter createHeaderWriter() {
        return new HeaderWriter(createCellWriter(), createSheetContext());
    }

    public TableWriter createTableWriter() {
        return new TableWriter(writableSheet, createSheetContext(), ExcelWritingSettings.DEFAULT);
    }

    public WritableSheetContext createSheetContext() {
        return new WritableSheetContext(new DefaultSerializationContext(), new DefaultStyleContext());
    }
}
