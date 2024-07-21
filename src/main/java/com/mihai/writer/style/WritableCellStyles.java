package com.mihai.writer.style;

import com.mihai.core.utils.DateFormatUtils;
import com.mihai.writer.style.border.CellBorders;
import com.mihai.writer.style.color.StyleColor;
import com.mihai.writer.style.font.StyleFonts;

/**
 * Provides some useful cell styles that can be used.
 *
 * @see WritableCellStyle
 */
public final class WritableCellStyles {

    private static final WritableCellStyle NO_STYLE = WritableCellStyle.builder().build();

    private static final WritableCellStyle BOLD_TEXT = WritableCellStyle.builder()
            .font(StyleFonts.bold())
            .build();

    private static final WritableCellStyle ALL_SIDE_BORDER = WritableCellStyle.builder()
            .border(CellBorders.allSidesThin())
            .build();

    private WritableCellStyles() {
        throw new IllegalStateException("Utility class");
    }

    public static WritableCellStyle noStyle() {
        return NO_STYLE;
    }

    /**
     * Formats the date according to the system locale.
     *
     * @return the cell style with date formatting.
     */
    public static WritableCellStyle forDate() {
        return WritableCellStyle.builder()
                .format(DateFormatUtils.getLocalizedDatePattern())
                .build();
    }

    /**
     * Makes the font inside the cell bold.
     *
     * @return bold text cell style.
     */
    public static WritableCellStyle boldText() {
        return BOLD_TEXT;
    }

    /**
     * Cell style which applies a thin border on all the sides of the cell.
     *
     * @return an all-side thin border cell style.
     */
    public static WritableCellStyle allSideBorder() {
        return ALL_SIDE_BORDER;
    }

    /**
     * Cell style which applies a background color with given rgb values to the cell.
     *
     * @param red   red value. Should be between 0 and 255, both inclusive.
     * @param green green value. Should be between 0 and 255, both inclusive.
     * @param blue  blue value. Should be between 0 and 255, both inclusive.
     * @return cell style which sets the background color.
     */
    public static WritableCellStyle backgroundColor(int red, int green, int blue) {
        return backgroundColor(new StyleColor(red, green, blue));
    }

    /**
     * Cell style which applies a background color to the cell.
     *
     * @param color color to be applied to the cell background.
     * @return cell style which sets the background color.
     */
    public static WritableCellStyle backgroundColor(StyleColor color) {
        return WritableCellStyle.builder()
                .backgroundColor(color)
                .build();
    }

    /**
     * Cell style which sets the cell number format. In Excel, this corresponds to: "Home" tab > "Number" section > In the dropdown - "More
     * number formats" > Custom.
     *
     * @param format the number format of the cell.
     * @return cell style which applies the cell number formatting.
     */
    public static WritableCellStyle format(String format) {
        return WritableCellStyle.builder()
                .format(format)
                .build();
    }
}
