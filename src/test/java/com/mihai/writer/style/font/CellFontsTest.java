package com.mihai.writer.style.font;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellFontsTest {

    @Test
    public void boldFontHasCorrespondingProperty() {
        assertTrue(CellFonts.bold().isBold());
    }

    @Test
    public void italicFontHasCorrespondingProperty() {
        assertTrue(CellFonts.italic().isItalic());
    }
}