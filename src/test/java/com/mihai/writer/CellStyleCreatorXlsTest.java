package com.mihai.writer;

import com.mihai.writer.style.WritableCellStyle;
import com.mihai.writer.style.border.CellBorder;
import com.mihai.writer.style.color.CellColor;
import com.mihai.writer.style.font.CellFont;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CellStyleCreatorXlsTest {

    private HSSFWorkbook workbook;
    private CellStyleCreator styleCreator;

    @BeforeEach
    public void setUp() {
        workbook = new HSSFWorkbook();
        styleCreator = new CellStyleCreator(workbook);
    }

    @AfterEach
    public void tearDown() throws IOException {
        workbook.close();
    }

    @Test
    public void borderApplied() {
        CellBorder border = CellBorder.builder()
                .topBorderStyle(BorderStyle.THIN)
                .rightBorderStyle(BorderStyle.MEDIUM)
                .bottomBorderStyle(BorderStyle.THICK)
                .leftBorderStyle(BorderStyle.THIN)
                .color(new CellColor(1, 2, 3))
                .build();
        CellStyle style = styleCreator.getCellStyle(WritableCellStyle.builder()
                .border(border)
                .build());
        assertEquals(BorderStyle.THIN, style.getBorderTop());
        assertEquals(BorderStyle.MEDIUM, style.getBorderRight());
        assertEquals(BorderStyle.THICK, style.getBorderBottom());
        assertEquals(BorderStyle.THIN, style.getBorderLeft());

        // for xls files, a similar color is returned from the existing color palette, as the palette is limited in size
        assertArrayEquals(new short[]{0, 0, 0}, getColorAtIndex(style.getTopBorderColor()).getTriplet());
        assertArrayEquals(new short[]{0, 0, 0}, getColorAtIndex(style.getRightBorderColor()).getTriplet());
        assertArrayEquals(new short[]{0, 0, 0}, getColorAtIndex(style.getBottomBorderColor()).getTriplet());
        assertArrayEquals(new short[]{0, 0, 0}, getColorAtIndex(style.getLeftBorderColor()).getTriplet());
    }

    @Test
    public void fontApplied() {
        CellFont font = CellFont.builder()
                .name("Calibri")
                .size((short) 18)
                .color(new CellColor(255, 255, 255))
                .bold(true)
                .italic(true)
                .underLine(true)
                .build();
        CellStyle style = styleCreator.getCellStyle(WritableCellStyle.builder()
                .font(font)
                .build());

        HSSFFont styleFont = getFontAtIndex(style.getFontIndex());

        assertEquals("Calibri", styleFont.getFontName());
        assertEquals(18, styleFont.getFontHeightInPoints());
        assertArrayEquals(new short[]{255, 255, 255}, styleFont.getHSSFColor(workbook).getTriplet());
        assertTrue(styleFont.getBold());
        assertTrue(styleFont.getItalic());
        assertEquals(Font.U_SINGLE, styleFont.getUnderline());
    }

    @Test
    public void backGroundColorApplied() {
        CellStyle style = styleCreator.getCellStyle(WritableCellStyle.builder()
                .backgroundColor(new CellColor(0, 0, 0))
                .build());
        assertArrayEquals(new short[]{0, 0, 0}, ((HSSFCellStyle)style).getFillForegroundColorColor().getTriplet());
        assertEquals(FillPatternType.SOLID_FOREGROUND, style.getFillPattern());
    }

    @Test
    public void textWrapApplied() {
        CellStyle style = styleCreator.getCellStyle(WritableCellStyle.builder()
                .wrapText(true)
                .build());
        assertTrue(style.getWrapText());
    }

    private HSSFFont getFontAtIndex(int index) {
        return workbook.getFontAt(index);
    }

    private HSSFColor getColorAtIndex(int index) {
        return workbook.getCustomPalette().getColor(index);
    }
}
