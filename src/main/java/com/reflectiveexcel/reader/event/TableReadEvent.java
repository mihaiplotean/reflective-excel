package com.reflectiveexcel.reader.event;

import com.reflectiveexcel.reader.ReadingContext;
import com.reflectiveexcel.reader.table.ReadTable;

public class TableReadEvent {

    private final ReadingContext readingContext;
    private final String tableId;
    private final ReadTable readTable;

    public TableReadEvent(ReadingContext readingContext, String tableId, ReadTable readTable) {
        this.readingContext = readingContext;
        this.tableId = tableId;
        this.readTable = readTable;
    }

    public ReadingContext getReadingContext() {
        return readingContext;
    }

    public String getTableId() {
        return tableId;
    }

    public ReadTable getReadTable() {
        return readTable;
    }
}
