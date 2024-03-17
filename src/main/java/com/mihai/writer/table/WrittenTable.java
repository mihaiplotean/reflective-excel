package com.mihai.writer.table;

import com.mihai.reader.workbook.sheet.Bounds;
import com.mihai.writer.locator.CellLocation;

public class WrittenTable {

    private final String id;
    private final WrittenTableHeaders headers;
    private final Bounds bounds;

    public WrittenTable(String id, WrittenTableHeaders headers, Bounds bounds) {
        this.id = id;
        this.headers = headers;
        this.bounds = bounds;
    }

    public String getId() {
        return id;
    }

    public int getHeadersRow() {
        return headers.getRow();
    }

    public int getColumnIndex(String headerName) {
        return headers.getColumnIndex(headerName);
    }

    public Bounds getBounds() {
        return bounds;
    }

    public CellLocation getTopLeftLocation() {
        return bounds.getTopLeftLocation();
    }

    public CellLocation getTopRightLocation() {
        return bounds.getTopRightLocation();
    }

    public CellLocation getBottomLeftLocation() {
        return bounds.getBottomLeftLocation();
    }

    public CellLocation getBottomRightLocation() {
        return bounds.getBottomRightLocation();
    }
}
