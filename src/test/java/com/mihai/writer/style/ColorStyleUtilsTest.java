package com.mihai.writer.style;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Test;

public class ColorStyleUtilsTest {

    @Test
    public void assertionErrorThrownOnUnknownCellStyle() {
        CellStyle unknownStyle = new CellStyle() {
            @Override
            public short getIndex() {
                return 0;
            }

            @Override
            public void setDataFormat(short fmt) {

            }

            @Override
            public short getDataFormat() {
                return 0;
            }

            @Override
            public String getDataFormatString() {
                return null;
            }

            @Override
            public void setFont(Font font) {

            }

            @Override
            public int getFontIndex() {
                return 0;
            }

            @Override
            public int getFontIndexAsInt() {
                return 0;
            }

            @Override
            public void setHidden(boolean hidden) {

            }

            @Override
            public boolean getHidden() {
                return false;
            }

            @Override
            public void setLocked(boolean locked) {

            }

            @Override
            public boolean getLocked() {
                return false;
            }

            @Override
            public void setQuotePrefixed(boolean quotePrefix) {

            }

            @Override
            public boolean getQuotePrefixed() {
                return false;
            }

            @Override
            public void setAlignment(HorizontalAlignment align) {

            }

            @Override
            public HorizontalAlignment getAlignment() {
                return null;
            }

            @Override
            public void setWrapText(boolean wrapped) {

            }

            @Override
            public boolean getWrapText() {
                return false;
            }

            @Override
            public void setVerticalAlignment(VerticalAlignment align) {

            }

            @Override
            public VerticalAlignment getVerticalAlignment() {
                return null;
            }

            @Override
            public void setRotation(short rotation) {

            }

            @Override
            public short getRotation() {
                return 0;
            }

            @Override
            public void setIndention(short indent) {

            }

            @Override
            public short getIndention() {
                return 0;
            }

            @Override
            public void setBorderLeft(BorderStyle border) {

            }

            @Override
            public BorderStyle getBorderLeft() {
                return null;
            }

            @Override
            public void setBorderRight(BorderStyle border) {

            }

            @Override
            public BorderStyle getBorderRight() {
                return null;
            }

            @Override
            public void setBorderTop(BorderStyle border) {

            }

            @Override
            public BorderStyle getBorderTop() {
                return null;
            }

            @Override
            public void setBorderBottom(BorderStyle border) {

            }

            @Override
            public BorderStyle getBorderBottom() {
                return null;
            }

            @Override
            public void setLeftBorderColor(short color) {

            }

            @Override
            public short getLeftBorderColor() {
                return 0;
            }

            @Override
            public void setRightBorderColor(short color) {

            }

            @Override
            public short getRightBorderColor() {
                return 0;
            }

            @Override
            public void setTopBorderColor(short color) {

            }

            @Override
            public short getTopBorderColor() {
                return 0;
            }

            @Override
            public void setBottomBorderColor(short color) {

            }

            @Override
            public short getBottomBorderColor() {
                return 0;
            }

            @Override
            public void setFillPattern(FillPatternType fp) {

            }

            @Override
            public FillPatternType getFillPattern() {
                return null;
            }

            @Override
            public void setFillBackgroundColor(short bg) {

            }

            @Override
            public void setFillBackgroundColor(Color color) {

            }

            @Override
            public short getFillBackgroundColor() {
                return 0;
            }

            @Override
            public Color getFillBackgroundColorColor() {
                return null;
            }

            @Override
            public void setFillForegroundColor(short bg) {

            }

            @Override
            public void setFillForegroundColor(Color color) {

            }

            @Override
            public short getFillForegroundColor() {
                return 0;
            }

            @Override
            public Color getFillForegroundColorColor() {
                return null;
            }

            @Override
            public void cloneStyleFrom(CellStyle source) {

            }

            @Override
            public void setShrinkToFit(boolean shrinkToFit) {

            }

            @Override
            public boolean getShrinkToFit() {
                return false;
            }
        };
        assertThrows(AssertionError.class, () -> ColorStyleUtils.setBorderColor(unknownStyle, new Color() {
        }));
    }

    @Test
    public void assertionErrorThrownOnUnknownFontStyle() {
        Font font = new Font() {

            @Override
            public void setFontName(String name) {

            }

            @Override
            public String getFontName() {
                return null;
            }

            @Override
            public void setFontHeight(short height) {

            }

            @Override
            public void setFontHeightInPoints(short height) {

            }

            @Override
            public short getFontHeight() {
                return 0;
            }

            @Override
            public short getFontHeightInPoints() {
                return 0;
            }

            @Override
            public void setItalic(boolean italic) {

            }

            @Override
            public boolean getItalic() {
                return false;
            }

            @Override
            public void setStrikeout(boolean strikeout) {

            }

            @Override
            public boolean getStrikeout() {
                return false;
            }

            @Override
            public void setColor(short color) {

            }

            @Override
            public short getColor() {
                return 0;
            }

            @Override
            public void setTypeOffset(short offset) {

            }

            @Override
            public short getTypeOffset() {
                return 0;
            }

            @Override
            public void setUnderline(byte underline) {

            }

            @Override
            public byte getUnderline() {
                return 0;
            }

            @Override
            public int getCharSet() {
                return 0;
            }

            @Override
            public void setCharSet(byte charset) {

            }

            @Override
            public void setCharSet(int charset) {

            }

            @Override
            public int getIndex() {
                return 0;
            }

            @Override
            public int getIndexAsInt() {
                return 0;
            }

            @Override
            public void setBold(boolean bold) {

            }

            @Override
            public boolean getBold() {
                return false;
            }
        };
        assertThrows(AssertionError.class, () -> ColorStyleUtils.setFontColor(font, new Color() {
        }));
    }
}
