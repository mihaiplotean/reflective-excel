package com.mihai.writer.style;

import com.mihai.writer.WritingContext;

/**
 * Class responsible for storing the cell style providers and for providing the cell style.
 * The order in which styles are combined is as follows:
 * <ol>
 *     <li>Header Style(when writing the table header)</li>
 *     <li>Row Style(when writing the table rows)</li>
 *     <li>Column Style(when writing the table rows)</li>
 *     <li>Type Style</li>
 *     <li>Cell Style</li>
 * </ol>
 */
public interface CellStyleContext {

    /**
     * Specifies the style provider for the table header cells.
     *
     * @param style the style provider.
     */
    void setHeaderStyleProvider(StyleProvider style);

    /**
     * Returns the style of the header to be written.
     *
     * @param context information related to the sheet's writing process.
     * @param headerValue the name/value of the header.
     * @return the style of the header.
     */
    WritableCellStyle getHeaderStyle(WritingContext context, Object headerValue);

    /**
     * Specifies the style provider for the table row cells.
     *
     * @param style the style provider.
     */
    void setRowStyleProvider(StyleProvider style);

    /**
     * Returns the style of the row to be written.
     *
     * @param context information related to the sheet's writing process.
     * @param row the row to be written.
     * @return the style of the row cells.
     */
    WritableCellStyle getRowStyle(WritingContext context, Object row);

    /**
     * Specifies the style provider for the table column cells.
     *
     * @param style the style provider.
     */
    void setColumnStyleProvider(StyleProvider style);

    /**
     * Returns the style of the column to be written.
     *
     * @param context information related to the sheet's writing process.
     * @param columnName the name/value of the header corresponding to the column.
     * @return the style of the column cells.
     */
    WritableCellStyle getColumnStyle(WritingContext context, Object columnName);

    /**
     * Specifies the style provider for given type.
     *
     * @param clazz the type the style provider is intended for.
     * @param style the style provider.
     */
    void setTypeStyleProvider(Class<?> clazz, StyleProvider style);

    /**
     * Returns the style of the type (before serialization) to be written.
     *
     * @param context information related to the sheet's writing process.
     * @param clazz the type of the value.
     * @param cellValue value of the cell.
     * @return the style of the column cells.
     */
    WritableCellStyle getTypeStyle(WritingContext context, Class<?> clazz, Object cellValue);

    /**
     * Specifies the style provider for each written cell.
     *
     * @param style the style provider.
     */
    void setCellStyleProvider(StyleProvider style);

    /**
     * Returns the style of the cell to be written.
     *
     * @param context information related to the sheet's writing process.
     * @param cellValue value of the cell.
     * @return the style of the cell.
     */
    WritableCellStyle getCellStyle(WritingContext context, Object cellValue);
}
