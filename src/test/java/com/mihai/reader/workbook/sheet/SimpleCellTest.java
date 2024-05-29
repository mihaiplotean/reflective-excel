package com.mihai.reader.workbook.sheet;

import com.mihai.core.workbook.Bounds;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SimpleCellTest {

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
    public void cellsAreEqualIfPOICellIsEqual() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        assertEquals(new SimpleCell(cell, "a"), new SimpleCell(cell, "b"));
    }

    @Test
    public void equalsSameObject() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        SimpleCell simpleCell = new SimpleCell(cell, "value");
        assertEquals(simpleCell, simpleCell);
    }

    @Test
    public void doesNotEqualNull() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        SimpleCell simpleCell = new SimpleCell(cell, "value");
        assertNotEquals(simpleCell, null);
    }

    @Test
    public void doesNotEqualDifferentClass() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        SimpleCell simpleCell = new SimpleCell(cell, "value");
        assertNotEquals(simpleCell, new Object());
    }

    @Test
    public void doesNotEqualIfPOICellIsDifferent() {
        Row row = actualSheet.createRow(0);
        Cell cellA = row.createCell(0);
        SimpleCell simpleCellA = new SimpleCell(cellA, "value");

        Cell cellB = row.createCell(1);
        SimpleCell simpleCellB = new SimpleCell(cellB, "value");

        assertNotEquals(simpleCellA, simpleCellB);
    }
}
