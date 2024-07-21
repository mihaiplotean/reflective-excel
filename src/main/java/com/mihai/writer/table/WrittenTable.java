package com.mihai.writer.table;

import com.mihai.core.workbook.Bounds;
import com.mihai.core.workbook.CellLocation;

/**
 * Information about the table which has been written to the sheet.
 */
public class WrittenTable {

    private final String id;
    private final WrittenTableHeaders headers;
    private final Bounds bounds;

    public WrittenTable(String id, WrittenTableHeaders headers, Bounds bounds) {
        this.id = id;
        this.headers = headers;
        this.bounds = bounds;
    }

    /**
     * Returns the id of the table.
     *
     * @return table id.
     * @see com.mihai.core.annotation.TableId
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the row index of the table headers. In case of grouped/nested headers, the
     * last row index is returned.
     *
     * @return the headers row index.
     */
    public int getHeadersRow() {
        return headers.getRow();
    }

    /**
     * Returns the column index of the header with the given name (case-insensitive).
     *
     * @param headerName name of the header.
     * @return the column index.
     */
    public int getColumnIndex(String headerName) {
        return headers.getColumnIndex(headerName);
    }

    /**
     * Returns the column name of the header at the given column.
     *
     * @param columnIndex the index of the header.
     * @return the column name.
     */
    public String getColumnName(int columnIndex) {
        return headers.getColumnName(columnIndex);
    }

    /**
     * Returns the bounds of the table.
     *
     * @return table bounds.
     */
    public Bounds getBounds() {
        return bounds;
    }

    /**
     * Returns the table top-left cell location.
     *
     * @return cell location.
     */
    public CellLocation getTopLeftLocation() {
        return bounds.getTopLeftLocation();
    }

    /**
     * Returns the table top-right cell location.
     *
     * @return cell location.
     */
    public CellLocation getTopRightLocation() {
        return bounds.getTopRightLocation();
    }

    /**
     * Returns the table bottom-left cell location.
     *
     * @return cell location.
     */
    public CellLocation getBottomLeftLocation() {
        return bounds.getBottomLeftLocation();
    }

    /**
     * Returns the table bottom-right cell location.
     *
     * @return cell location.
     */
    public CellLocation getBottomRightLocation() {
        return bounds.getBottomRightLocation();
    }
}
