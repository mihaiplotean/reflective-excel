package com.mihai.reader;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReadTables implements Iterable<ReadTable> {

    private final Map<String, ReadTable> tablePerId = new LinkedHashMap<>();

    private ReadTable lastTable;

    public void append(ReadTable table) {
        tablePerId.put(table.getId(), table);
        lastTable = table;
    }

    public ReadTable getTable(String id) {
        return tablePerId.get(id);
    }

    public ReadTable getLastTable() {
        return lastTable;
    }

    @Override
    public Iterator<ReadTable> iterator() {
        return tablePerId.values().iterator();
    }
}
