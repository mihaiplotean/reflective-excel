package com.mihai.writer.style;

import com.mihai.writer.WritingContext;

/**
 * Used in combination with {@link CellStyleContext} to provide a cell style to be applied.
 *
 * @see CellStyleContext
 */
public interface StyleProvider {

    /**
     * Returns the cell style for a given target - header, row, column, type or cell. This style can be different
     * based on the target or the current writing context.
     *
     * @param context information related to the sheet's writing process.
     * @param target  When applied to the header - the header value.
     *                When applied to the row - the row object.
     *                When applied to the column - the header value.
     *                When applied to a type - the cell value.
     *                When applied to a cell - the cell value.
     * @return the cell style to be applied.
     */
    WritableCellStyle getStyle(WritingContext context, Object target);

    default StyleProvider andThen(StyleProvider other) {
        return (context, target) -> StyleProvider.this.getStyle(context, target)
                .combineWith(other.getStyle(context, target));
    }
}
