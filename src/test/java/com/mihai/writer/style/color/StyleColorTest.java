package com.mihai.writer.style.color;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StyleColorTest {

    @Test
    public void colorValuesCorrectlySet() {
        StyleColor color = new StyleColor(42, 10, 22);
        assertEquals(42, color.getRed());
        assertEquals(10, color.getGreen());
        assertEquals(22, color.getBlue());
    }

    @Test
    public void colorValuesOutsideRangeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new StyleColor(300, 300, 300));
    }

    @Test
    public void colorNegativeValuesOutsideRangeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new StyleColor(-1, -1, -1));
    }
}