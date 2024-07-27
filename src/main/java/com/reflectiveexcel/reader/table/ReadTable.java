package com.reflectiveexcel.reader.table;

import com.reflectiveexcel.core.workbook.Bounds;

/**
 * Table which has already been read. Can be useful when multiple tables are read at once
 * using {@link com.reflectiveexcel.reader.ReflectiveExcelReader#read(Class)} and you need the information about a previously
 * read table.
 */
public class ReadTable {

    private final String id;
    private final TableHeaders headers;
    private final Bounds bounds;

    public ReadTable(String id, TableHeaders headers, Bounds bounds) {
        this.id = id;
        this.headers = headers;
        this.bounds = bounds;
    }

    public String getId() {
        return id;
    }

    public TableHeaders getHeaders() {
        return headers;
    }

    public Bounds getBounds() {
        return bounds;
    }
}
