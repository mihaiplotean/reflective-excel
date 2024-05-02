package com.mihai.reader;

import com.mihai.reader.workbook.sheet.Bounds;
import com.mihai.writer.table.WrittenTableHeaders;

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
