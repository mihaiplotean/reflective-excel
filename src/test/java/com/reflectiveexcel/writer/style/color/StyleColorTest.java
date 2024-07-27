package com.reflectiveexcel.writer.style.color;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class StyleColorTest {

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

    @Test
    public void equalsSameObject() {
        StyleColor color = new StyleColor(1, 1, 1);
        assertEquals(color, color);
    }

    @Test
    public void doesNotEqualNull() {
        StyleColor color = new StyleColor(1, 1, 1);
        assertNotEquals(color, null);
    }

    @Test
    public void doesNotEqualIfRedValueIsDifferent() {
        StyleColor colorA = new StyleColor(1, 1, 1);
        StyleColor colorB = new StyleColor(2, 1, 1);

        assertNotEquals(colorA, colorB);
    }

    @Test
    public void doesNotEqualIfGreenValueIsDifferent() {
        StyleColor colorA = new StyleColor(1, 1, 1);
        StyleColor colorB = new StyleColor(1, 2, 1);

        assertNotEquals(colorA, colorB);
    }

    @Test
    public void doesNotEqualIfBlueValueIsDifferent() {
        StyleColor colorA = new StyleColor(1, 1, 1);
        StyleColor colorB = new StyleColor(1, 1, 2);

        assertNotEquals(colorA, colorB);
    }
}
