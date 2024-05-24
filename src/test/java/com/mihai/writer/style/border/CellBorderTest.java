package com.mihai.writer.style.border;

import com.mihai.writer.style.color.StyleColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CellBorderTest {

    @Test
    public void creatingBorderUsingBuilderSetsValues() {
        CellBorder border = CellBorder.builder()
                .topBorderStyle(BorderStyle.THIN)
                .rightBorderStyle(BorderStyle.MEDIUM)
                .bottomBorderStyle(BorderStyle.THICK)
                .leftBorderStyle(BorderStyle.NONE)
                .color(new StyleColor(1, 1, 1))
                .build();
        assertEquals(BorderStyle.THIN, border.getTopBorderStyle());
        assertEquals(BorderStyle.MEDIUM, border.getRightBorderStyle());
        assertEquals(BorderStyle.THICK, border.getBottomBorderStyle());
        assertEquals(BorderStyle.NONE, border.getLeftBorderStyle());
        assertEquals(new StyleColor(1, 1, 1), border.getColor());
    }

    @Test
    public void createdFromBorderStyleHasSameBorderOnAllSides() {
        CellBorder border = new CellBorder(BorderStyle.THIN);
        assertEquals(BorderStyle.THIN, border.getTopBorderStyle());
        assertEquals(BorderStyle.THIN, border.getRightBorderStyle());
        assertEquals(BorderStyle.THIN, border.getBottomBorderStyle());
        assertEquals(BorderStyle.THIN, border.getLeftBorderStyle());
    }

    @Test
    public void combiningTwoBordersKeepsPropertiesOfFirstOne() {
        CellBorder thinBorderStyle = CellBorder.builder()
                .topBorderStyle(BorderStyle.THIN)
                .rightBorderStyle(BorderStyle.THIN)
                .bottomBorderStyle(BorderStyle.THIN)
                .leftBorderStyle(BorderStyle.THIN)
                .color(StyleColor.BLACK)
                .build();
        CellBorder mediumBorderStyle = CellBorder.builder()
                .topBorderStyle(BorderStyle.MEDIUM)
                .rightBorderStyle(BorderStyle.MEDIUM)
                .bottomBorderStyle(BorderStyle.MEDIUM)
                .leftBorderStyle(BorderStyle.MEDIUM)
                .color(new StyleColor(1, 1, 1))
                .build();
        CellBorder combinedBorder = thinBorderStyle.combineWith(mediumBorderStyle);
        assertEquals(thinBorderStyle, combinedBorder);
    }

    @Test
    public void combiningTwoBordersPopulatesProperties() {
        CellBorder borderA = CellBorder.builder()
                .topBorderStyle(BorderStyle.THIN)
                .rightBorderStyle(null)
                .bottomBorderStyle(null)
                .leftBorderStyle(null)
                .color(null)
                .build();
        CellBorder borderB = CellBorder.builder()
                .topBorderStyle(null)
                .rightBorderStyle(BorderStyle.MEDIUM)
                .bottomBorderStyle(BorderStyle.MEDIUM)
                .leftBorderStyle(BorderStyle.MEDIUM)
                .color(new StyleColor(1, 1, 1))
                .build();
        CellBorder expectedBorder = CellBorder.builder()
                .topBorderStyle(BorderStyle.THIN)
                .rightBorderStyle(BorderStyle.MEDIUM)
                .bottomBorderStyle(BorderStyle.MEDIUM)
                .leftBorderStyle(BorderStyle.MEDIUM)
                .color(new StyleColor(1, 1, 1))
                .build();
        assertEquals(expectedBorder, borderA.combineWith(borderB));
    }
}
