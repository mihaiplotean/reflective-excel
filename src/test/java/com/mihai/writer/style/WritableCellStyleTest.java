package com.mihai.writer.style;

import com.mihai.writer.style.border.CellBorder;
import com.mihai.writer.style.border.CellBorders;
import com.mihai.writer.style.color.CellColor;
import com.mihai.writer.style.font.CellFont;
import com.mihai.writer.style.font.CellFonts;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
                .backgroundColor(CellColor.BLACK)
                .build();
        assertEquals(CellColor.BLACK, cellStyle.getBackgroundColor());
    }

    @Test
    public void fontIsSet() {
        WritableCellStyle cellStyle = WritableCellStyle.builder()
                .font(CellFonts.bold())
                .build();
        assertEquals(CellFonts.bold(), cellStyle.getFont());
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
                .backgroundColor(new CellColor(1, 1, 1))
                .build();
        WritableCellStyle cellStyleB = WritableCellStyle.builder()
                .backgroundColor(new CellColor(2, 2, 2))
                .build();
        assertEquals(new CellColor(1, 1, 1), cellStyleA.combineWith(cellStyleB).getBackgroundColor());
    }

    @Test
    public void combiningFontsKeepsFirst() {
        WritableCellStyle cellStyleA = WritableCellStyle.builder()
                .font(CellFonts.bold())
                .build();
        WritableCellStyle cellStyleB = WritableCellStyle.builder()
                .font(CellFonts.italic())
                .build();
        assertEquals(CellFonts.bold(), cellStyleA.combineWith(cellStyleB).getFont());
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
}
