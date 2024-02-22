package com.mihai.writer.style.font;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class CellFont {

    private final String name;
    private final short size;  // Font#setFontHeightInPoints

    private final boolean bold;
    private final boolean italic;
    private final boolean underLine;

    private CellFont(CellFontBuilder cellFontBuilder) {
        name = cellFontBuilder.name;
        size = cellFontBuilder.size;
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
                && italic == cellFont.italic
                && underLine == cellFont.underLine;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size, bold, italic, underLine);
    }

    public static final class CellFontBuilder {

        private String name;
        private short size;

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
