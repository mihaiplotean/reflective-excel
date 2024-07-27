package com.reflectiveexcel.writer.style.border;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.junit.jupiter.api.Test;

public class CellBordersTest {

    @Test
    public void allSidesThingBorderCorrectlyCreated() {
        CellBorder border = CellBorders.allSidesThin();
        assertEquals(BorderStyle.THIN, border.getTopBorderStyle());
        assertEquals(BorderStyle.THIN, border.getRightBorderStyle());
        assertEquals(BorderStyle.THIN, border.getBottomBorderStyle());
        assertEquals(BorderStyle.THIN, border.getLeftBorderStyle());
    }
}
