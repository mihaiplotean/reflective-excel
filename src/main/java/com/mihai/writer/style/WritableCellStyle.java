package com.mihai.writer.style;

import java.util.Objects;

import com.mihai.writer.style.border.CellBorder;
import com.mihai.writer.style.border.CellBorders;
import com.mihai.writer.style.color.StyleColor;
import com.mihai.writer.style.font.StyleFont;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class WritableCellStyle {

    private final String format;
    private final HorizontalAlignment horizontalAlignment;
    private final VerticalAlignment verticalAlignment;
    private final CellBorder border;
    private final StyleColor backgroundColor;
    private final StyleFont font;
    private final boolean wrapText;

    private WritableCellStyle(CellStyleBuilder builder) {
        format = builder.format;
        horizontalAlignment = builder.horizontalAlignment;
        verticalAlignment = builder.verticalAlignment;
        border = builder.border;
        backgroundColor = builder.backgroundColor;
        font = builder.font;
        wrapText = builder.wrapText;
    }

    public String getFormat() {
        return format;
    }

    public HorizontalAlignment getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    public CellBorder getBorder() {
        return border;
    }

    public StyleColor getBackgroundColor() {
        return backgroundColor;
    }

    public StyleFont getFont() {
        return font;
    }

    public boolean isWrapText() {
        return wrapText;
    }

    public WritableCellStyle combineWith(WritableCellStyle other) {
        return WritableCellStyle.builder()
                .format(!StringUtils.isEmpty(format) ? format : other.format)
                .horizontalAlignment(horizontalAlignment != null ? horizontalAlignment : other.horizontalAlignment)
                .verticalAlignment(verticalAlignment != null ? verticalAlignment : other.verticalAlignment)
                .border(combinedBorder(other.border))
                .backgroundColor(backgroundColor != null ? backgroundColor : other.backgroundColor)
                .font(combinedFont(other.font))
                .wrapText(wrapText || other.wrapText)
                .build();
    }

    private CellBorder combinedBorder(CellBorder other) {
        if (this.border == null) {
            return other;
        }
        if (other == null) {
            return this.border;
        }
        return border.combineWith(other);
    }

    private StyleFont combinedFont(StyleFont other) {
        if (this.font == null) {
            return other;
        }
        if (other == null) {
            return this.font;
        }
        return font.combineWith(other);
    }

    public static CellStyleBuilder builder() {
        return new CellStyleBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WritableCellStyle cellStyle = (WritableCellStyle) o;
        return wrapText == cellStyle.wrapText
                && horizontalAlignment == cellStyle.horizontalAlignment
                && verticalAlignment == cellStyle.verticalAlignment
                && Objects.equals(format, cellStyle.format)
                && Objects.equals(border, cellStyle.border)
                && Objects.equals(backgroundColor, cellStyle.backgroundColor)
                && Objects.equals(font, cellStyle.font);
    }

    @Override
    public int hashCode() {
        return Objects.hash(format, horizontalAlignment, verticalAlignment, border, backgroundColor, font, wrapText);
    }

    public static final class CellStyleBuilder {

        private String format;
        private HorizontalAlignment horizontalAlignment;
        private VerticalAlignment verticalAlignment;
        private CellBorder border;
        private StyleColor backgroundColor;
        private StyleFont font;
        private boolean wrapText;

        public CellStyleBuilder format(String format) {
            this.format = format;
            return this;
        }

        public CellStyleBuilder horizontalAlignment(HorizontalAlignment horizontalAlignment) {
            this.horizontalAlignment = horizontalAlignment;
            return this;
        }

        public CellStyleBuilder verticalAlignment(VerticalAlignment verticalAlignment) {
            this.verticalAlignment = verticalAlignment;
            return this;
        }

        public CellStyleBuilder border(CellBorder border) {
            this.border = border;
            return this;
        }

        public CellStyleBuilder backgroundColor(StyleColor backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public CellStyleBuilder allSideBorder(BorderStyle borderStyle) {
            this.border = new CellBorder(borderStyle);
            return this;
        }

        public CellStyleBuilder thinAllSideBorder() {
            this.border = CellBorders.allSidesThin();
            return this;
        }

        public CellStyleBuilder font(StyleFont font) {
            this.font = font;
            return this;
        }

        public CellStyleBuilder wrapText(boolean wrapText) {
            this.wrapText = wrapText;
            return this;
        }

        public WritableCellStyle build() {
            return new WritableCellStyle(this);
        }
    }
}
