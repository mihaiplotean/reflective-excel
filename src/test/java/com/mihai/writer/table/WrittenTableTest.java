package com.mihai.writer.table;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class WrittenTableTest {

    @Test
    public void columnNameRetrieval() {
        WrittenTableHeaders headers = new WrittenTableHeaders(1, List.of(
                new WrittenTableHeader("A", 1),
                new WrittenTableHeader("B", 2)
        ));
        WrittenTable table = new WrittenTable("A", headers, null);
        assertEquals("B", table.getColumnName(2));
    }

    @Test
    public void columnIndexRetrievalIsCaseInsensitive() {
        WrittenTableHeaders headers = new WrittenTableHeaders(1, List.of(
                new WrittenTableHeader("A", 1),
                new WrittenTableHeader("B", 2)
        ));
        WrittenTable table = new WrittenTable("A", headers, null);
        assertEquals(1, table.getColumnIndex("a"));
    }
}
