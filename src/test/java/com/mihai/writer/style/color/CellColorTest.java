package com.mihai.writer.style.color;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellColorTest {

    @Test
    public void colorValuesCorrectlySet() {
        CellColor color = new CellColor(42, 10, 22);
        assertEquals(42, color.getRed());
        assertEquals(10, color.getGreen());
        assertEquals(22, color.getBlue());
    }

    @Test
    public void colorValuesOutsideRangeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new CellColor(300, 300, 300));
    }

    @Test
    public void colorNegativeValuesOutsideRangeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new CellColor(-1, -1, -1));
    }
}