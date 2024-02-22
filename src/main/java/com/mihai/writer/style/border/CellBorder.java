package com.mihai.writer.style.border;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.Objects;

public class CellBorder {

    private final BorderStyle topBorderStyle;
    private final BorderStyle rightBorderStyle;
    private final BorderStyle bottomBorderStyle;
    private final BorderStyle leftBorderStyle;

    private final IndexedColors color;  // how to make this a custom color? Possible idea: creationHelper.createExtendedColor().getIndex()

    public CellBorder(BorderStyle borderStyle) {
        topBorderStyle = borderStyle;
        rightBorderStyle = borderStyle;
        bottomBorderStyle = borderStyle;
        leftBorderStyle = borderStyle;
        color = null;
    }

    private CellBorder(CellBorderBuilder builder) {
        topBorderStyle = builder.topBorderStyle;
        rightBorderStyle = builder.rightBorderStyle;
        bottomBorderStyle = builder.bottomBorderStyle;
        leftBorderStyle = builder.leftBorderStyle;
        color = builder.color;
    }

    public BorderStyle getTopBorderStyle() {
        return topBorderStyle;
    }

    public BorderStyle getRightBorderStyle() {
        return rightBorderStyle;
    }

    public BorderStyle getBottomBorderStyle() {
        return bottomBorderStyle;
    }

    public BorderStyle getLeftBorderStyle() {
        return leftBorderStyle;
    }

    public IndexedColors getColor() {
        return color;
    }

    public CellBorder combineWith(CellBorder other) {
        return new CellBorderBuilder()
                .topBorderStyle(topBorderStyle != null ? topBorderStyle : other.topBorderStyle)
                .rightBorderStyle(rightBorderStyle != null ? rightBorderStyle : other.rightBorderStyle)
                .bottomBorderStyle(bottomBorderStyle != null ? bottomBorderStyle : other.bottomBorderStyle)
                .leftBorderStyle(leftBorderStyle != null ? leftBorderStyle : other.leftBorderStyle)
                .color(color != null ? color : other.color)
                .build();
    }

    public static CellBorderBuilder builder() {
        return new CellBorderBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellBorder that = (CellBorder) o;
        return topBorderStyle == that.topBorderStyle
                && rightBorderStyle == that.rightBorderStyle
                && bottomBorderStyle == that.bottomBorderStyle
                && leftBorderStyle == that.leftBorderStyle
                && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(topBorderStyle, rightBorderStyle, bottomBorderStyle, leftBorderStyle, color);
    }

    public static final class CellBorderBuilder {

        private BorderStyle topBorderStyle;
        private BorderStyle rightBorderStyle;
        private BorderStyle bottomBorderStyle;
        private BorderStyle leftBorderStyle;

        private IndexedColors color;

        public CellBorderBuilder topBorderStyle(BorderStyle topBorderStyle) {
            this.topBorderStyle = topBorderStyle;
            return this;
        }

        public CellBorderBuilder rightBorderStyle(BorderStyle rightBorderStyle) {
            this.rightBorderStyle = rightBorderStyle;
            return this;
        }

        public CellBorderBuilder bottomBorderStyle(BorderStyle bottomBorderStyle) {
            this.bottomBorderStyle = bottomBorderStyle;
            return this;
        }

        public CellBorderBuilder leftBorderStyle(BorderStyle leftBorderStyle) {
            this.leftBorderStyle = leftBorderStyle;
            return this;
        }

        public CellBorderBuilder color(IndexedColors color) {
            this.color = color;
            return this;
        }

        public CellBorder build() {
            return new CellBorder(this);
        }
    }
}
