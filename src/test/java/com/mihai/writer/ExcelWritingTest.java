package com.mihai.writer;

import java.io.IOException;

import com.mihai.writer.serializer.DefaultSerializationContext;
import com.mihai.writer.style.DefaultStyleContext;
import com.mihai.writer.writers.CellWriter;
import com.mihai.writer.writers.HeaderWriter;
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
        return new HeaderWriter(createCellWriter(),
                                new WritableSheetContext(new DefaultSerializationContext(), new DefaultStyleContext()));
    }
}
