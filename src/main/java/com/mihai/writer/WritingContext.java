package com.mihai.writer;

import com.mihai.writer.table.WrittenTable;
import com.mihai.writer.table.WrittenTables;

public class WritingContext {

    private final WrittenTables tables;

    public WritingContext(WrittenTables tables) {
        this.tables = tables;
    }

    /**
     * @param tableId id of the table
     * @return the table bounds of a table which has already been written.
     */
    public WrittenTable getTable(String tableId) {
        return tables.getTable(tableId);
    }

    public WrittenTable getLastTable() {
        return tables.getLastTable();
    }
}
