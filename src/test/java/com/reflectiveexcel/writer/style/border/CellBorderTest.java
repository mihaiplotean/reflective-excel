package com.reflectiveexcel.writer.style.border;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.reflectiveexcel.writer.style.color.StyleColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.junit.jupiter.api.Test;

public class CellBorderTest {

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

    @Test
    public void equalsSameObject() {
        CellBorder border = new CellBorder(BorderStyle.THIN);
        assertEquals(border, border);
    }

    @Test
    public void doesNotEqualNull() {
        CellBorder border = new CellBorder(BorderStyle.THIN);
        assertNotEquals(border, null);
    }

    @Test
    public void doesNotEqualIfLeftBorderIsDifferent() {
        CellBorder borderA = CellBorder.builder()
                .leftBorderStyle(BorderStyle.THIN)
                .topBorderStyle(BorderStyle.THIN)
                .rightBorderStyle(BorderStyle.THIN)
                .bottomBorderStyle(BorderStyle.THIN)
                .build();
        CellBorder borderB = CellBorder.builder()
                .leftBorderStyle(BorderStyle.MEDIUM)
                .topBorderStyle(BorderStyle.THIN)
                .rightBorderStyle(BorderStyle.THIN)
                .bottomBorderStyle(BorderStyle.THIN)
                .build();
        assertNotEquals(borderA, borderB);
    }

    @Test
    public void doesNotEqualIfTopBorderIsDifferent() {
        CellBorder borderA = CellBorder.builder()
                .leftBorderStyle(BorderStyle.THIN)
                .topBorderStyle(BorderStyle.THIN)
                .rightBorderStyle(BorderStyle.THIN)
                .bottomBorderStyle(BorderStyle.THIN)
                .build();
        CellBorder borderB = CellBorder.builder()
                .leftBorderStyle(BorderStyle.THIN)
                .topBorderStyle(BorderStyle.MEDIUM)
                .rightBorderStyle(BorderStyle.THIN)
                .bottomBorderStyle(BorderStyle.THIN)
                .build();
        assertNotEquals(borderA, borderB);
    }

    @Test
    public void doesNotEqualIfRightBorderIsDifferent() {
        CellBorder borderA = CellBorder.builder()
                .leftBorderStyle(BorderStyle.THIN)
                .topBorderStyle(BorderStyle.THIN)
                .rightBorderStyle(BorderStyle.THIN)
                .bottomBorderStyle(BorderStyle.THIN)
                .build();
        CellBorder borderB = CellBorder.builder()
                .leftBorderStyle(BorderStyle.THIN)
                .topBorderStyle(BorderStyle.THIN)
                .rightBorderStyle(BorderStyle.MEDIUM)
                .bottomBorderStyle(BorderStyle.THIN)
                .build();
        assertNotEquals(borderA, borderB);
    }

    @Test
    public void doesNotEqualIfBottomBorderIsDifferent() {
        CellBorder borderA = CellBorder.builder()
                .leftBorderStyle(BorderStyle.THIN)
                .topBorderStyle(BorderStyle.THIN)
                .rightBorderStyle(BorderStyle.THIN)
                .bottomBorderStyle(BorderStyle.THIN)
                .build();
        CellBorder borderB = CellBorder.builder()
                .leftBorderStyle(BorderStyle.THIN)
                .topBorderStyle(BorderStyle.THIN)
                .rightBorderStyle(BorderStyle.THIN)
                .bottomBorderStyle(BorderStyle.MEDIUM)
                .build();
        assertNotEquals(borderA, borderB);
    }
}
