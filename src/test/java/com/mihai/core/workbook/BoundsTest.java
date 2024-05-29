package com.mihai.core.workbook;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoundsTest {

    @Test
    public void invalidRowBoundsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Bounds(1, 0, 0, 0));
    }

    @Test
    public void invalidColumnBoundsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Bounds(0, 1, 0, 0));
    }

    @Test
    public void boundsNotEqualToNull() {
        assertNotEquals(null, new Bounds(1, 1, 1, 1));
    }

    @Test
    public void sameBoundsAreEqualByProperties() {
        assertEquals(new Bounds(1, 1, 1, 1), new Bounds(1, 1, 1, 1));
    }
}