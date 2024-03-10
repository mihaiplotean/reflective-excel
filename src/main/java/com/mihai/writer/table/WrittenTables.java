package com.mihai.writer.table;

import java.util.HashMap;
import java.util.Map;

public class WrittenTables {

    private final Map<String, WrittenTable> tablePerId = new HashMap<>();

    private WrittenTable lastTable;

    public void append(WrittenTable table) {
        tablePerId.put(table.getId(), table);
        lastTable = table;
    }

    public WrittenTable getTable(String id) {
        return tablePerId.get(id);
    }

    public WrittenTable getLastTable() {
        return lastTable;
    }
}
