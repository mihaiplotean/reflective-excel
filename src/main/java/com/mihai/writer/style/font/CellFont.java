package com.mihai.writer.style.font;

import com.mihai.writer.style.color.CellColor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.Objects;

public class CellFont {

    private final String name;
    private final short size;  // Font#setFontHeightInPoints
    private final CellColor color;

    private final boolean bold;
    private final boolean italic;
    private final boolean underLine;

    private CellFont(CellFontBuilder cellFontBuilder) {
        name = cellFontBuilder.name;
        size = cellFontBuilder.size;
        color = cellFontBuilder.color;
        bold = cellFontBuilder.bold;
        italic = cellFontBuilder.italic;
        underLine = cellFontBuilder.underLine;
    }

    public String getName() {
        return name;
    }

    public short getSize() {
        return size;
    }

    public CellColor getColor() {
        return color;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public boolean isUnderLine() {
        return underLine;
    }

    public CellFont combineWith(CellFont other) {
        return new CellFontBuilder()
                .name(!StringUtils.isEmpty(name) ? name : other.name)
                .size(size > 0 ? size : other.size)
                .color(color != null ? color : other.color)
                .bold(bold || other.bold)
                .italic(italic || other.italic)
                .underLine(underLine || other.underLine)
                .build();
    }

    public static CellFontBuilder builder() {
        return new CellFontBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellFont cellFont = (CellFont) o;
        return Objects.equals(name, cellFont.name)
                && size == cellFont.size
                && bold == cellFont.bold
                && Objects.equals(color, cellFont.color)
                && italic == cellFont.italic
                && underLine == cellFont.underLine;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size, bold, italic, underLine);
    }

    @Override
    public String toString() {
        return "CellFont{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", color=" + color +
                ", bold=" + bold +
                ", italic=" + italic +
                ", underLine=" + underLine +
                '}';
    }

    public static final class CellFontBuilder {

        private String name;
        private short size;
        private CellColor color;

        private boolean bold;
        private boolean italic;
        private boolean underLine;

        public CellFontBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CellFontBuilder size(short size) {
            this.size = size;
            return this;
        }

        public CellFontBuilder color(CellColor color) {
            this.color = color;
            return this;
        }

        public CellFontBuilder bold(boolean bold) {
            this.bold = bold;
            return this;
        }

        public CellFontBuilder italic(boolean italic) {
            this.italic = italic;
            return this;
        }

        public CellFontBuilder underLine(boolean underLine) {
            this.underLine = underLine;
            return this;
        }

        public CellFont build() {
            return new CellFont(this);
        }
    }
}
