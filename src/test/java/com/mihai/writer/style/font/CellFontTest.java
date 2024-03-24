package com.mihai.writer.style.font;

import com.mihai.writer.style.color.CellColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellFontTest {

    @Test
    public void creatingFontUsingBuilderSetsValues() {
        CellFont font = CellFont.builder()
                .bold(true)
                .underLine(true)
                .size((short) 12)
                .name("Arial")
                .color(CellColor.BLACK)
                .build();
        assertTrue(font.isBold());
        assertFalse(font.isItalic());
        assertTrue(font.isUnderLine());
        assertEquals(12, font.getSize());
        assertEquals("Arial", font.getName());
        assertEquals(CellColor.BLACK, font.getColor());
    }

    @Test
    public void combiningTwoFontsKeepsBoldProperty() {
        CellFont nonBoldFont = CellFont.builder().bold(false).build();
        CellFont boldFont = CellFont.builder().bold(true).build();
        assertTrue(nonBoldFont.combineWith(boldFont).isBold());
    }

    @Test
    public void combiningTwoFontsKeepsItalicProperty() {
        CellFont nonItalicFont = CellFont.builder().italic(false).build();
        CellFont italicFont = CellFont.builder().italic(true).build();
        assertTrue(nonItalicFont.combineWith(italicFont).isItalic());
    }

    @Test
    public void combiningTwoFontsKeepsUnderlineProperty() {
        CellFont nonUnderlinedFont = CellFont.builder().underLine(false).build();
        CellFont underlinedFont = CellFont.builder().underLine(true).build();
        assertTrue(nonUnderlinedFont.combineWith(underlinedFont).isUnderLine());
    }

    @Test
    public void combiningTwoFontsKeepsFirstFontSize() {
        CellFont fontA = CellFont.builder().size((short) 12).build();
        CellFont fontB = CellFont.builder().size((short) 14).build();
        assertEquals(12, fontA.combineWith(fontB).getSize());
    }

    @Test
    public void combiningTwoFontsKeepsPositiveFontSize() {
        CellFont fontA = CellFont.builder().size((short) 0).build();
        CellFont fontB = CellFont.builder().size((short) 14).build();
        assertEquals(14, fontA.combineWith(fontB).getSize());
    }

    @Test
    public void combiningTwoFontsKeepsFirstFontName() {
        CellFont fontA = CellFont.builder().name("Arial").build();
        CellFont fontB = CellFont.builder().name("Calibri").build();
        assertEquals("Arial", fontA.combineWith(fontB).getName());
    }

    @Test
    public void combiningTwoFontsKeepsNonEmptyFontName() {
        CellFont fontA = CellFont.builder().name("").build();
        CellFont fontB = CellFont.builder().name("Calibri").build();
        assertEquals("Calibri", fontA.combineWith(fontB).getName());
    }

    @Test
    public void combiningTwoFontsKeepsFirstColor() {
        CellFont fontA = CellFont.builder().color(new CellColor(1, 1, 1)).build();
        CellFont fontB = CellFont.builder().color(new CellColor(2, 2, 2)).build();
        assertEquals(new CellColor(1, 1, 1), fontA.combineWith(fontB).getColor());
    }
}