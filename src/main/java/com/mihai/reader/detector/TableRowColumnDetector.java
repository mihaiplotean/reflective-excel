package com.mihai.reader.detector;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.workbook.sheet.ReadableRow;

/**
 * Used to determine where the table to be read starts and ends, as well as which rows to skip while reading it.
 */
public interface TableRowColumnDetector {

    /**
     * Used to determine in which row the table header is.
     *
     * @param context information related to the sheet's reading process.
     * @param row the row to be checked if it represents the table header row.
     * @return true if the provided row belongs to the table header.
     */
    boolean isHeaderRow(ReadingContext context, ReadableRow row);

    /**
     * After the header row has been found, this method is used to check if a given cell from that row is the
     * header starting cell.
     *
     * @param context information related to the sheet's reading process.
     * @param cell the cell to be checked if it is the first header cell.
     * @return true if the provided cell is the first header cell.
     */
    boolean isHeaderStartColumn(ReadingContext context, ReadableCell cell);

    /**
     * After the header row and header stating cell have been found, this method is used to check which cell from the
     * header row is the final header cell.
     *
     * @param context information related to the sheet's reading process.
     * @param cell the cell to be checked if it is the last header cell.
     * @return true if the provided cell is the first last cell.
     */
    boolean isHeaderLastColumn(ReadingContext context, ReadableCell cell);

    /**
     * After the header row, as where does it start and end has been determined, this method is used to check
     * which rows within the table should be skipped. The row is bounded based on the determined header, meaning that
     * it won't contain cells outside the table bounds.
     *
     * @param context information related to the sheet's reading process.
     * @param tableRow the row to be checked if it should be skipped.
     * @return true if the provided row should be skipped.
     */
    boolean shouldSkipRow(ReadingContext context, ReadableRow tableRow);

    /**
     * This method is used to check where the table ends.
     *
     * @param context information related to the sheet's reading process.
     * @param row the row to be checked if it is the last table rows.
     * @return true if the provided row is the last table row.
     */
    boolean isLastRow(ReadingContext context, ReadableRow row);
}
