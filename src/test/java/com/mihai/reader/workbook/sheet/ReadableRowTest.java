package com.mihai.reader.workbook.sheet;

import com.mihai.core.workbook.Bounds;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReadableRowTest {

    private XSSFWorkbook workbook;
    private XSSFSheet actualSheet;

    @BeforeEach
    public void setUp() {
        workbook = new XSSFWorkbook();
        actualSheet = workbook.createSheet();
    }

    @AfterEach
    public void tearDown() throws IOException {
        workbook.close();
    }

    @Test
    public void rowsAreEqualIfCellsAndRowNumberIsEqual() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        assertEquals(new ReadableRow(1, List.of(new SimpleCell(cell, "value"))),
                new ReadableRow(1, List.of(new SimpleCell(cell, "value"))));
    }

    @Test
    public void equalsSameObject() {
        ReadableRow row = new ReadableRow(1, List.of());
        assertEquals(row, row);
    }

    @Test
    public void doesNotEqualNull() {
        ReadableRow row = new ReadableRow(1, List.of());
        assertNotEquals(row, null);
    }

    @Test
    public void doesNotEqualDifferentClass() {
        ReadableRow row = new ReadableRow(1, List.of());
        assertNotEquals(row, new Object());
    }

    @Test
    public void doesNotEqualIfRowNumberIsDifferent() {
        ReadableRow rowA = new ReadableRow(1, List.of());
        ReadableRow rowB = new ReadableRow(2, List.of());

        assertNotEquals(rowA, rowB);
    }

    @Test
    public void doesNotEqualIfCellsAreDifferent() {
        Cell cellA = actualSheet.createRow(0).createCell(0);
        Cell cellB = actualSheet.createRow(1).createCell(1);

        ReadableRow rowA = new ReadableRow(1, List.of(new SimpleCell(cellA, "")));
        ReadableRow rowB = new ReadableRow(1, List.of(new SimpleCell(cellB, "")));

        assertNotEquals(rowA, rowB);
    }
}