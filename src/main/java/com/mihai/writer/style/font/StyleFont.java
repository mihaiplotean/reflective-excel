package com.mihai.writer.style.font;

import com.mihai.writer.style.color.StyleColor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class StyleFont {

    private final String name;
    private final short size;
    private final StyleColor color;

    private final boolean bold;
    private final boolean italic;
    private final boolean underLine;

    private StyleFont(CellFontBuilder cellFontBuilder) {
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

    public StyleColor getColor() {
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

    public StyleFont combineWith(StyleFont other) {
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
        StyleFont styleFont = (StyleFont) o;
        return Objects.equals(name, styleFont.name)
                && size == styleFont.size
                && bold == styleFont.bold
                && Objects.equals(color, styleFont.color)
                && italic == styleFont.italic
                && underLine == styleFont.underLine;
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
        private StyleColor color;

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

        public CellFontBuilder color(StyleColor color) {
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

        public StyleFont build() {
            return new StyleFont(this);
        }
    }
}
