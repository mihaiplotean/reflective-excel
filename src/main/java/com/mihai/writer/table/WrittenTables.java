package com.mihai.writer.table;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class WrittenTables implements Iterable<WrittenTable> {

    private final Map<String, WrittenTable> tablePerId = new LinkedHashMap<>();

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

    @Override
    public Iterator<WrittenTable> iterator() {
        return tablePerId.values().iterator();
    }
}
