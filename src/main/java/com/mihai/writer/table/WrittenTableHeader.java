package com.mihai.writer.table;

public class WrittenTableHeader {

    private final String name;
    private final int column;

    public WrittenTableHeader(String name, int column) {
        this.column = column;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getColumn() {
        return column;
    }
}
