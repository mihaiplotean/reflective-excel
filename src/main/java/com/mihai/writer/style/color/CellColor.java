package com.mihai.writer.style.color;

import com.mihai.writer.style.font.CellFont;

import java.util.Objects;

public class CellColor {

    public static final CellColor BLACK = new CellColor((byte) 0, (byte) 0, (byte) 0);

    private final byte red;
    private final byte green;
    private final byte blue;

    public CellColor(byte red, byte green, byte blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public byte getRed() {
        return red;
    }

    public byte getGreen() {
        return green;
    }

    public byte getBlue() {
        return blue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellColor cellColor = (CellColor) o;
        return red == cellColor.red && green == cellColor.green && blue == cellColor.blue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue);
    }
}
