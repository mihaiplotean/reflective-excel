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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MergedCellTest {

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
        assertEquals(new MergedCell(cell, "a", new Bounds(0, 0, 0, 0)),
                new MergedCell(cell, "b", new Bounds(0, 0, 0, 0)));
    }

    @Test
    public void equalsSameObject() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        MergedCell mergedCell = new MergedCell(cell, "value", new Bounds(1, 1, 1, 1));
        assertEquals(mergedCell, mergedCell);
    }

    @Test
    public void doesNotEqualNull() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        MergedCell mergedCell = new MergedCell(cell, "value", new Bounds(1, 1, 1, 1));
        assertNotEquals(mergedCell, null);
    }

    @Test
    public void doesNotEqualDifferentClass() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        MergedCell mergedCell = new MergedCell(cell, "value", new Bounds(1, 1, 1, 1));
        assertNotEquals(mergedCell, new Object());
    }

    @Test
    public void doesNotEqualIfPOICellIsDifferent() {
        Row row = actualSheet.createRow(0);
        Cell cellA = row.createCell(0);
        MergedCell mergedCellA = new MergedCell(cellA, "value", new Bounds(1, 1, 1, 1));

        Cell cellB = row.createCell(1);
        MergedCell mergedCellB = new MergedCell(cellB, "value", new Bounds(1, 1, 1, 1));

        assertNotEquals(mergedCellA, mergedCellB);
    }

    @Test
    public void doesNotEqualIfBoundsAreDifferent() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        MergedCell mergedCellA = new MergedCell(cell, "value", new Bounds(1, 1, 1, 1));
        MergedCell mergedCellB = new MergedCell(cell, "value", new Bounds(0, 0, 0, 0));

        assertNotEquals(mergedCellA, mergedCellB);
    }
}
