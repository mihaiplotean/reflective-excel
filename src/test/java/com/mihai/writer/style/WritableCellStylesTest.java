package com.mihai.writer.style;

import static org.junit.jupiter.api.Assertions.*;

import com.mihai.writer.style.border.CellBorders;
import com.mihai.writer.style.color.StyleColor;
import org.junit.jupiter.api.Test;

public class WritableCellStylesTest {

    @Test
    public void dateStyleFormatIsNotEmpty() {
        // locale dependant, thus simply checking that some value is set
        assertFalse(WritableCellStyles.forDate().getFormat().isEmpty());
    }

    @Test
    public void boldTextStyleHasBoldFont() {
        assertTrue(WritableCellStyles.boldText().getFont().isBold());
    }

    @Test
    public void allSideBorderStyleHasThinBorder() {
        assertEquals(CellBorders.allSidesThin(), WritableCellStyles.allSideBorder().getBorder());
    }

    @Test
    public void backgroundColorStyleHasColorValues() {
        assertEquals(new StyleColor(1, 2, 3), WritableCellStyles.backgroundColor(1, 2, 3).getBackgroundColor());
    }

    @Test
    public void styleCreatedWithFormatSavesValue() {
        assertEquals("abc", WritableCellStyles.format("abc").getFormat());
    }
}
