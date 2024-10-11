package com.reflectiveexcel.writer.table;

import java.util.Collections;
import java.util.List;

public class WrittenTableHeaders {

    private final int row;
    private final List<WrittenTableHeader> headers;

    public WrittenTableHeaders(int row, List<WrittenTableHeader> headers) {
        this.row = row;
        this.headers = headers;
    }

    /**
     * Returns the row index of the table headers. In case of grouped/nested headers, the
     * last row index is returned.
     *
     * @return the headers row index.
     */
    public int getRow() {
        return row;
    }

    public List<WrittenTableHeader> getLeafHeaders() {
        return Collections.unmodifiableList(headers);
    }

    public String getColumnName(int columnIndex) {
        return headers.stream()
                .filter(header -> header.getColumn() == columnIndex)
                .map(WrittenTableHeader::getName)
                .findFirst()
                .orElse("");
    }

    public int getColumnIndex(String headerName) {
        return headers.stream()
                .filter(header -> header.getName().equalsIgnoreCase(headerName))
                .map(WrittenTableHeader::getColumn)
                .findFirst()
                .orElse(-1);
    }
}
