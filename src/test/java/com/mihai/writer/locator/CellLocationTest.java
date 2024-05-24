package com.mihai.writer.locator;

import com.mihai.common.workbook.CellLocation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellLocationTest {

    @Test
    public void createdFromReferenceGivesCorrectRowAndColumn() {
        CellLocation cellLocation = CellLocation.fromReference("C10");
        assertEquals(9, cellLocation.getRow());
        assertEquals(2, cellLocation.getColumn());
    }

    @Test
    public void createdFromInvalidReferenceThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> CellLocation.fromReference("ABC"));
    }

    @Test
    public void getRowGivesCorrectValue() {
        CellLocation cellLocation = new CellLocation(42, 12);
        assertEquals(42, cellLocation.getRow());
    }

    @Test
    public void getColumnGivesCorrectValue() {
        CellLocation cellLocation = new CellLocation(42, 12);
        assertEquals(12, cellLocation.getColumn());
    }

    @Test
    public void correctCellOnTheLeft() {
        CellLocation cellLocation = new CellLocation(42, 12).getLeftBy(2);
        assertEquals(42, cellLocation.getRow());
        assertEquals(10, cellLocation.getColumn());
    }

    @Test
    public void correctCellOnTheRight() {
        CellLocation cellLocation = new CellLocation(42, 12).getRightBy(2);
        assertEquals(42, cellLocation.getRow());
        assertEquals(14, cellLocation.getColumn());
    }

    @Test
    public void correctCellAbove() {
        CellLocation cellLocation = new CellLocation(42, 12).getUpBy(2);
        assertEquals(40, cellLocation.getRow());
        assertEquals(12, cellLocation.getColumn());
    }

    @Test
    public void correctCellBellow() {
        CellLocation cellLocation = new CellLocation(42, 12).getDownBy(2);
        assertEquals(44, cellLocation.getRow());
        assertEquals(12, cellLocation.getColumn());
    }

    @Test
    public void correctCellBellowAndOnTheRight() {
        CellLocation cellLocation = new CellLocation(42, 12).getOffsetBy(2, 2);
        assertEquals(44, cellLocation.getRow());
        assertEquals(14, cellLocation.getColumn());
    }

    @Test
    public void correctCellAboveAndOnTheLeft() {
        CellLocation cellLocation = new CellLocation(42, 12).getOffsetBy(-2, -2);
        assertEquals(40, cellLocation.getRow());
        assertEquals(10, cellLocation.getColumn());
    }

    @Test
    public void createdWithRowColumnEqualsCreatedWithReference() {
        assertEquals(new CellLocation(9, 2), CellLocation.fromReference("C10"));
    }
}