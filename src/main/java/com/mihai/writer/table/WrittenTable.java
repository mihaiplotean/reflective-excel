package com.mihai.writer.table;

import com.mihai.reader.workbook.sheet.Bounds;
import com.mihai.writer.locator.CellLocation;

public class WrittenTable {

    private final String id;
    private final Bounds bounds;

    public WrittenTable(String id, Bounds bounds) {
        this.id = id;
        this.bounds = bounds;
    }

    public String getId() {
        return id;
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
