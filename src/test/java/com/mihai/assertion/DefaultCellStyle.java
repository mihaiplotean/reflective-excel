package com.mihai.assertion;

import org.apache.poi.ss.usermodel.*;

public class DefaultCellStyle implements CellStyle {

    @Override
    public short getIndex() {
        return 0;
    }

    @Override
    public void setDataFormat(short fmt) {
        // do nothing
    }

    @Override
    public short getDataFormat() {
        return 0;
    }

    @Override
    public String getDataFormatString() {
        return "";
    }

    @Override
    public void setFont(Font font) {
        // do nothing
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
        // do nothing
    }

    @Override
    public boolean getHidden() {
        return false;
    }

    @Override
    public void setLocked(boolean locked) {
        // do nothing
    }

    @Override
    public boolean getLocked() {
        return false;
    }

    @Override
    public void setQuotePrefixed(boolean quotePrefix) {
        // do nothing
    }

    @Override
    public boolean getQuotePrefixed() {
        return false;
    }

    @Override
    public void setAlignment(HorizontalAlignment align) {
        // do nothing
    }

    @Override
    public HorizontalAlignment getAlignment() {
        return HorizontalAlignment.CENTER;
    }

    @Override
    public void setWrapText(boolean wrapped) {
        // do nothing
    }

    @Override
    public boolean getWrapText() {
        return false;
    }

    @Override
    public void setVerticalAlignment(VerticalAlignment align) {
        // do nothing
    }

    @Override
    public VerticalAlignment getVerticalAlignment() {
        return VerticalAlignment.BOTTOM;
    }

    @Override
    public void setRotation(short rotation) {
        // do nothing
    }

    @Override
    public short getRotation() {
        return 0;
    }

    @Override
    public void setIndention(short indent) {
        // do nothing
    }

    @Override
    public short getIndention() {
        return 0;
    }

    @Override
    public void setBorderLeft(BorderStyle border) {
        // do nothing
    }

    @Override
    public BorderStyle getBorderLeft() {
        return BorderStyle.NONE;
    }

    @Override
    public void setBorderRight(BorderStyle border) {
        // do nothing
    }

    @Override
    public BorderStyle getBorderRight() {
        return BorderStyle.NONE;
    }

    @Override
    public void setBorderTop(BorderStyle border) {
        // do nothing
    }

    @Override
    public BorderStyle getBorderTop() {
        return BorderStyle.NONE;
    }

    @Override
    public void setBorderBottom(BorderStyle border) {
        // do nothing
    }

    @Override
    public BorderStyle getBorderBottom() {
        return BorderStyle.NONE;
    }

    @Override
    public void setLeftBorderColor(short color) {
        // do nothing
    }

    @Override
    public short getLeftBorderColor() {
        return 0;
    }

    @Override
    public void setRightBorderColor(short color) {
        // do nothing
    }

    @Override
    public short getRightBorderColor() {
        return 0;
    }

    @Override
    public void setTopBorderColor(short color) {
        // do nothing
    }

    @Override
    public short getTopBorderColor() {
        return 0;
    }

    @Override
    public void setBottomBorderColor(short color) {
        // do nothing
    }

    @Override
    public short getBottomBorderColor() {
        return 0;
    }

    @Override
    public void setFillPattern(FillPatternType fp) {
        // do nothing
    }

    @Override
    public FillPatternType getFillPattern() {
        return FillPatternType.NO_FILL;
    }

    @Override
    public void setFillBackgroundColor(short bg) {
        // do nothing
    }

    @Override
    public void setFillBackgroundColor(Color color) {
        // do nothing
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
        // do nothing
    }

    @Override
    public void setFillForegroundColor(Color color) {
        // do nothing
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
        // do nothing
    }

    @Override
    public void setShrinkToFit(boolean shrinkToFit) {
        // do nothing
    }

    @Override
    public boolean getShrinkToFit() {
        return false;
    }
}
