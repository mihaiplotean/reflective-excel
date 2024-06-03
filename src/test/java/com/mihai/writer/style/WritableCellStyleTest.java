package com.mihai.writer.style;

import static org.junit.jupiter.api.Assertions.*;

import com.mihai.writer.style.border.CellBorder;
import com.mihai.writer.style.border.CellBorders;
import com.mihai.writer.style.color.StyleColor;
import com.mihai.writer.style.font.StyleFont;
import com.mihai.writer.style.font.StyleFonts;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.junit.jupiter.api.Test;

class WritableCellStyleTest {

    @Test
    public void formatIsSet() {
        WritableCellStyle cellStyle = WritableCellStyle.builder()
                .format("abc")
                .build();
        assertEquals("abc", cellStyle.getFormat());
    }

    @Test
    public void horizontalAlignmentIsSet() {
        WritableCellStyle cellStyle = WritableCellStyle.builder()
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .build();
        assertEquals(HorizontalAlignment.CENTER, cellStyle.getHorizontalAlignment());
    }

    @Test
    public void verticalAlignmentIsSet() {
        WritableCellStyle cellStyle = WritableCellStyle.builder()
                .verticalAlignment(VerticalAlignment.BOTTOM)
                .build();
        assertEquals(VerticalAlignment.BOTTOM, cellStyle.getVerticalAlignment());
    }

    @Test
    public void borderIsSet() {
        WritableCellStyle cellStyle = WritableCellStyle.builder()
                .border(CellBorders.allSidesThin())
                .build();
        assertEquals(CellBorders.allSidesThin(), cellStyle.getBorder());
    }

    @Test
    public void backgroundColorIsSet() {
        WritableCellStyle cellStyle = WritableCellStyle.builder()
                .backgroundColor(StyleColor.BLACK)
                .build();
        assertEquals(StyleColor.BLACK, cellStyle.getBackgroundColor());
    }

    @Test
    public void fontIsSet() {
        WritableCellStyle cellStyle = WritableCellStyle.builder()
                .font(StyleFonts.bold())
                .build();
        assertEquals(StyleFonts.bold(), cellStyle.getFont());
    }

    @Test
    public void wrapTextIsSet() {
        WritableCellStyle cellStyle = WritableCellStyle.builder()
                .wrapText(true)
                .build();
        assertTrue(cellStyle.isWrapText());
    }

    @Test
    public void combiningFormatsKeepsFirst() {
        WritableCellStyle cellStyleA = WritableCellStyle.builder()
                .format("abc")
                .build();
        WritableCellStyle cellStyleB = WritableCellStyle.builder()
                .format("xyz")
                .build();
        assertEquals("abc", cellStyleA.combineWith(cellStyleB).getFormat());
    }

    @Test
    public void combiningHorizontalAlignmentsKeepsFirst() {
        WritableCellStyle cellStyleA = WritableCellStyle.builder()
                .horizontalAlignment(HorizontalAlignment.LEFT)
                .build();
        WritableCellStyle cellStyleB = WritableCellStyle.builder()
                .horizontalAlignment(HorizontalAlignment.RIGHT)
                .build();
        assertEquals(HorizontalAlignment.LEFT, cellStyleA.combineWith(cellStyleB).getHorizontalAlignment());
    }

    @Test
    public void combiningVerticalAlignmentsKeepsFirst() {
        WritableCellStyle cellStyleA = WritableCellStyle.builder()
                .verticalAlignment(VerticalAlignment.BOTTOM)
                .build();
        WritableCellStyle cellStyleB = WritableCellStyle.builder()
                .verticalAlignment(VerticalAlignment.TOP)
                .build();
        assertEquals(VerticalAlignment.BOTTOM, cellStyleA.combineWith(cellStyleB).getVerticalAlignment());
    }

    @Test
    public void combiningBordersKeepsFirst() {
        WritableCellStyle cellStyleA = WritableCellStyle.builder()
                .allSideBorder(BorderStyle.THICK)
                .build();
        WritableCellStyle cellStyleB = WritableCellStyle.builder()
                .allSideBorder(BorderStyle.THIN)
                .build();
        assertEquals(new CellBorder(BorderStyle.THICK), cellStyleA.combineWith(cellStyleB).getBorder());
    }

    @Test
    public void combiningBackgroundColorsKeepsFirst() {
        WritableCellStyle cellStyleA = WritableCellStyle.builder()
                .backgroundColor(new StyleColor(1, 1, 1))
                .build();
        WritableCellStyle cellStyleB = WritableCellStyle.builder()
                .backgroundColor(new StyleColor(2, 2, 2))
                .build();
        assertEquals(new StyleColor(1, 1, 1), cellStyleA.combineWith(cellStyleB).getBackgroundColor());
    }

    @Test
    public void combiningFontsKeepsFirst() {
        WritableCellStyle cellStyleA = WritableCellStyle.builder()
                .font(StyleFonts.bold())
                .build();
        WritableCellStyle cellStyleB = WritableCellStyle.builder()
                .font(StyleFont.builder().bold(false).build())
                .build();
        assertEquals(StyleFonts.bold(), cellStyleA.combineWith(cellStyleB).getFont());
    }

    @Test
    public void combiningStylesKeepsWrapTextProperty() {
        WritableCellStyle cellStyleA = WritableCellStyle.builder()
                .wrapText(false)
                .build();
        WritableCellStyle cellStyleB = WritableCellStyle.builder()
                .wrapText(true)
                .build();
        assertTrue(cellStyleA.combineWith(cellStyleB).isWrapText());
    }

    @Test
    public void equalsSameObject() {
        WritableCellStyle style = WritableCellStyle.builder().build();
        assertEquals(style, style);
    }

    @Test
    public void doesNotEqualNull() {
        WritableCellStyle style = WritableCellStyle.builder().build();
        assertNotEquals(style, null);
    }

    @Test
    public void doesNotEqualIfFormatIsDifferent() {
        WritableCellStyle styleA = WritableCellStyle.builder().format("ab").build();
        WritableCellStyle styleB = WritableCellStyle.builder().format("abc").build();
        assertNotEquals(styleA, styleB);
    }

    @Test
    public void doesNotEqualIfHorizontalAlignmentIsDifferent() {
        WritableCellStyle styleA = WritableCellStyle.builder()
                .horizontalAlignment(HorizontalAlignment.LEFT)
                .build();
        WritableCellStyle styleB = WritableCellStyle.builder()
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .build();
        assertNotEquals(styleA, styleB);
    }

    @Test
    public void doesNotEqualIfVerticalAlignmentIsDifferent() {
        WritableCellStyle styleA = WritableCellStyle.builder()
                .verticalAlignment(VerticalAlignment.BOTTOM)
                .build();
        WritableCellStyle styleB = WritableCellStyle.builder()
                .verticalAlignment(VerticalAlignment.CENTER)
                .build();
        assertNotEquals(styleA, styleB);
    }

    @Test
    public void doesNotEqualIfBorderIsDifferent() {
        WritableCellStyle styleA = WritableCellStyle.builder()
                .border(new CellBorder(BorderStyle.THIN))
                .build();
        WritableCellStyle styleB = WritableCellStyle.builder()
                .border(new CellBorder(BorderStyle.THICK))
                .build();
        assertNotEquals(styleA, styleB);
    }

    @Test
    public void doesNotEqualIfBackgroundColorIsDifferent() {
        WritableCellStyle styleA = WritableCellStyle.builder()
                .backgroundColor(new StyleColor(1, 1, 1))
                .build();
        WritableCellStyle styleB = WritableCellStyle.builder()
                .backgroundColor(new StyleColor(1, 0, 1))
                .build();
        assertNotEquals(styleA, styleB);
    }

    @Test
    public void doesNotEqualIfFontIsDifferent() {
        WritableCellStyle styleA = WritableCellStyle.builder()
                .font(StyleFonts.bold())
                .build();
        WritableCellStyle styleB = WritableCellStyle.builder()
                .font(StyleFonts.italic())
                .build();
        assertNotEquals(styleA, styleB);
    }

    @Test
    public void doesNotEqualIfTextWrapPropertyIsDifferent() {
        WritableCellStyle styleA = WritableCellStyle.builder()
                .wrapText(true)
                .build();
        WritableCellStyle styleB = WritableCellStyle.builder()
                .wrapText(false)
                .build();
        assertNotEquals(styleA, styleB);
    }
}
