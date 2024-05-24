package com.mihai.writer;

import com.mihai.writer.style.WritableCellStyle;
import com.mihai.writer.style.border.CellBorder;
import com.mihai.writer.style.color.StyleColor;
import com.mihai.writer.style.font.StyleFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CellStyleCreatorXlsxTest {

    private XSSFWorkbook workbook;
    private CellStyleCreator styleCreator;

    @BeforeEach
    public void setUp() {
        workbook = new XSSFWorkbook();
        styleCreator = new CellStyleCreator(workbook);
    }

    @AfterEach
    public void tearDown() throws IOException {
        workbook.close();
    }

    @Test
    public void styleDataFormatApplied() {
        CellStyle style = styleCreator.getCellStyle(WritableCellStyle.builder()
                .format("abc")
                .build());
        assertEquals("abc", style.getDataFormatString());
    }

    @Test
    public void horizontalAlignmentApplied() {
        CellStyle style = styleCreator.getCellStyle(WritableCellStyle.builder()
                .horizontalAlignment(HorizontalAlignment.LEFT)
                .build());
        assertEquals(HorizontalAlignment.LEFT, style.getAlignment());
    }

    @Test
    public void verticalAlignmentApplied() {
        CellStyle style = styleCreator.getCellStyle(WritableCellStyle.builder()
                .verticalAlignment(VerticalAlignment.BOTTOM)
                .build());
        assertEquals(VerticalAlignment.BOTTOM, style.getVerticalAlignment());
    }

    @Test
    public void borderApplied() {
        CellBorder border = CellBorder.builder()
                .topBorderStyle(BorderStyle.THIN)
                .rightBorderStyle(BorderStyle.MEDIUM)
                .bottomBorderStyle(BorderStyle.THICK)
                .leftBorderStyle(BorderStyle.THIN)
                .color(new StyleColor(1, 2, 3))
                .build();
        CellStyle style = styleCreator.getCellStyle(WritableCellStyle.builder()
                .border(border)
                .build());
        assertEquals(BorderStyle.THIN, style.getBorderTop());
        assertEquals(BorderStyle.MEDIUM, style.getBorderRight());
        assertEquals(BorderStyle.THICK, style.getBorderBottom());
        assertEquals(BorderStyle.THIN, style.getBorderLeft());

        assertArrayEquals(new byte[]{1, 2, 3}, ((XSSFCellStyle) style).getTopBorderXSSFColor().getRGB());
        assertArrayEquals(new byte[]{1, 2, 3}, ((XSSFCellStyle) style).getRightBorderXSSFColor().getRGB());
        assertArrayEquals(new byte[]{1, 2, 3}, ((XSSFCellStyle) style).getBottomBorderXSSFColor().getRGB());
        assertArrayEquals(new byte[]{1, 2, 3}, ((XSSFCellStyle) style).getLeftBorderXSSFColor().getRGB());
    }

    @Test
    public void fontApplied() {
        StyleFont font = StyleFont.builder()
                .name("Calibri")
                .size((short) 18)
                .color(new StyleColor(1, 2, 3))
                .bold(true)
                .italic(true)
                .underLine(true)
                .build();
        CellStyle style = styleCreator.getCellStyle(WritableCellStyle.builder()
                .font(font)
                .build());

        XSSFFont styleFont = getFontAtIndex(style.getFontIndex());

        assertEquals("Calibri", styleFont.getFontName());
        assertEquals(18, styleFont.getFontHeightInPoints());
        assertArrayEquals(new byte[]{1, 2, 3}, styleFont.getXSSFColor().getRGB());
        assertTrue(styleFont.getBold());
        assertTrue(styleFont.getItalic());
        assertEquals(Font.U_SINGLE, styleFont.getUnderline());
    }

    @Test
    public void backGroundColorApplied() {
        CellStyle style = styleCreator.getCellStyle(WritableCellStyle.builder()
                .backgroundColor(new StyleColor(1, 2, 3))
                .build());
        assertArrayEquals(new byte[]{1, 2, 3}, ((XSSFColor) style.getFillForegroundColorColor()).getRGB());
        assertEquals(FillPatternType.SOLID_FOREGROUND, style.getFillPattern());
    }

    @Test
    public void textWrapApplied() {
        CellStyle style = styleCreator.getCellStyle(WritableCellStyle.builder()
                .wrapText(true)
                .build());
        assertTrue(style.getWrapText());
    }

    private XSSFFont getFontAtIndex(int index) {
        return workbook.getStylesSource().getFontAt(index);
    }
}
