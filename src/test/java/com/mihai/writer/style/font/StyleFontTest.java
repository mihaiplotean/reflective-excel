package com.mihai.writer.style.font;

import static org.junit.jupiter.api.Assertions.*;

import com.mihai.writer.style.color.StyleColor;
import org.junit.jupiter.api.Test;

class StyleFontTest {

    @Test
    public void creatingFontUsingBuilderSetsValues() {
        StyleFont font = StyleFont.builder()
                .bold(true)
                .underLine(true)
                .size(12)
                .name("Arial")
                .color(StyleColor.BLACK)
                .build();
        assertTrue(font.isBold());
        assertFalse(font.isItalic());
        assertTrue(font.isUnderLine());
        assertEquals(12, font.getSize());
        assertEquals("Arial", font.getName());
        assertEquals(StyleColor.BLACK, font.getColor());
    }

    @Test
    public void combiningTwoFontsKeepsBoldProperty() {
        StyleFont nonBoldFont = StyleFont.builder().bold(false).build();
        StyleFont boldFont = StyleFont.builder().bold(true).build();
        assertTrue(nonBoldFont.combineWith(boldFont).isBold());
    }

    @Test
    public void combiningTwoFontsKeepsItalicProperty() {
        StyleFont nonItalicFont = StyleFont.builder().italic(false).build();
        StyleFont italicFont = StyleFont.builder().italic(true).build();
        assertTrue(nonItalicFont.combineWith(italicFont).isItalic());
    }

    @Test
    public void combiningTwoFontsKeepsUnderlineProperty() {
        StyleFont nonUnderlinedFont = StyleFont.builder().underLine(false).build();
        StyleFont underlinedFont = StyleFont.builder().underLine(true).build();
        assertTrue(nonUnderlinedFont.combineWith(underlinedFont).isUnderLine());
    }

    @Test
    public void combiningTwoFontsKeepsFirstFontSize() {
        StyleFont fontA = StyleFont.builder().size(12).build();
        StyleFont fontB = StyleFont.builder().size(14).build();
        assertEquals(12, fontA.combineWith(fontB).getSize());
    }

    @Test
    public void combiningTwoFontsKeepsPositiveFontSize() {
        StyleFont fontA = StyleFont.builder().size(0).build();
        StyleFont fontB = StyleFont.builder().size(14).build();
        assertEquals(14, fontA.combineWith(fontB).getSize());
    }

    @Test
    public void combiningTwoFontsKeepsFirstFontName() {
        StyleFont fontA = StyleFont.builder().name("Arial").build();
        StyleFont fontB = StyleFont.builder().name("Calibri").build();
        assertEquals("Arial", fontA.combineWith(fontB).getName());
    }

    @Test
    public void combiningTwoFontsKeepsNonEmptyFontName() {
        StyleFont fontA = StyleFont.builder().name("").build();
        StyleFont fontB = StyleFont.builder().name("Calibri").build();
        assertEquals("Calibri", fontA.combineWith(fontB).getName());
    }

    @Test
    public void combiningTwoFontsKeepsFirstColor() {
        StyleFont fontA = StyleFont.builder().color(new StyleColor(1, 1, 1)).build();
        StyleFont fontB = StyleFont.builder().color(new StyleColor(2, 2, 2)).build();
        assertEquals(new StyleColor(1, 1, 1), fontA.combineWith(fontB).getColor());
    }

    @Test
    public void equalsSameObject() {
        StyleFont font = StyleFont.builder().build();
        assertEquals(font, font);
    }

    @Test
    public void doesNotEqualNull() {
        StyleFont font = StyleFont.builder().build();
        assertNotEquals(font, null);
    }

    @Test
    public void doesNotEqualIfNameIsDifferent() {
        StyleFont fontA = StyleFont.builder().name("Arial").build();
        StyleFont fontB = StyleFont.builder().name("Calibri").build();

        assertNotEquals(fontA, fontB);
    }

    @Test
    public void doesNotEqualIfSizeIsDifferent() {
        StyleFont fontA = StyleFont.builder().size(12).build();
        StyleFont fontB = StyleFont.builder().size(14).build();

        assertNotEquals(fontA, fontB);
    }

    @Test
    public void doesNotEqualIfColorIsDifferent() {
        StyleFont fontA = StyleFont.builder().color(new StyleColor(1, 1, 1)).build();
        StyleFont fontB = StyleFont.builder().color(new StyleColor(1, 0, 1)).build();

        assertNotEquals(fontA, fontB);
    }

    @Test
    public void doesNotEqualIfBoldnessIsDifferent() {
        StyleFont fontA = StyleFont.builder().bold(true).build();
        StyleFont fontB = StyleFont.builder().bold(false).build();

        assertNotEquals(fontA, fontB);
    }

    @Test
    public void doesNotEqualIfItalicPropertyIsDifferent() {
        StyleFont fontA = StyleFont.builder().italic(true).build();
        StyleFont fontB = StyleFont.builder().italic(false).build();

        assertNotEquals(fontA, fontB);
    }

    @Test
    public void doesNotEqualIfUnderlineIsDifferent() {
        StyleFont fontA = StyleFont.builder().underLine(true).build();
        StyleFont fontB = StyleFont.builder().underLine(false).build();

        assertNotEquals(fontA, fontB);
    }
}
