package com.reflectiveexcel.writer.locator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.reflectiveexcel.core.workbook.CellLocation;
import org.junit.jupiter.api.Test;

public class CellLocationTest {

    @Test
    public void createdFromReferenceGivesCorrectRowAndColumn() {
        CellLocation cellLocation = CellLocation.fromReference("C10");
        assertEquals(9, cellLocation.row());
        assertEquals(2, cellLocation.column());
    }

    @Test
    public void createdFromInvalidReferenceThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> CellLocation.fromReference("ABC"));
    }

    @Test
    public void createdFromInvalidReferenceThrowsException2() {
        assertThrows(IllegalArgumentException.class, () -> CellLocation.fromReference("123"));
    }

    @Test
    public void createdFromNegativeRowThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new CellLocation(-1, 0));
    }

    @Test
    public void createdFromNegativeColumnThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new CellLocation(0, -1));
    }

    @Test
    public void getRowGivesCorrectValue() {
        CellLocation cellLocation = new CellLocation(42, 12);
        assertEquals(42, cellLocation.row());
    }

    @Test
    public void getColumnGivesCorrectValue() {
        CellLocation cellLocation = new CellLocation(42, 12);
        assertEquals(12, cellLocation.column());
    }

    @Test
    public void correctCellOnTheLeft() {
        CellLocation cellLocation = new CellLocation(42, 12).getLeftBy(2);
        assertEquals(42, cellLocation.row());
        assertEquals(10, cellLocation.column());
    }

    @Test
    public void correctCellOnTheRight() {
        CellLocation cellLocation = new CellLocation(42, 12).getRightBy(2);
        assertEquals(42, cellLocation.row());
        assertEquals(14, cellLocation.column());
    }

    @Test
    public void correctCellAbove() {
        CellLocation cellLocation = new CellLocation(42, 12).getUpBy(2);
        assertEquals(40, cellLocation.row());
        assertEquals(12, cellLocation.column());
    }

    @Test
    public void correctCellBellow() {
        CellLocation cellLocation = new CellLocation(42, 12).getDownBy(2);
        assertEquals(44, cellLocation.row());
        assertEquals(12, cellLocation.column());
    }

    @Test
    public void correctCellBellowAndOnTheRight() {
        CellLocation cellLocation = new CellLocation(42, 12).getOffsetBy(2, 2);
        assertEquals(44, cellLocation.row());
        assertEquals(14, cellLocation.column());
    }

    @Test
    public void correctCellAboveAndOnTheLeft() {
        CellLocation cellLocation = new CellLocation(42, 12).getOffsetBy(-2, -2);
        assertEquals(40, cellLocation.row());
        assertEquals(10, cellLocation.column());
    }

    @Test
    public void createdWithRowColumnEqualsCreatedWithReference() {
        assertEquals(new CellLocation(9, 2), CellLocation.fromReference("C10"));
    }
}
