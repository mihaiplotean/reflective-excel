package com.reflectiveexcel.reader.event;

import com.reflectiveexcel.reader.ReadingContext;

public class RowSkippedEvent {

    private final ReadingContext readingContext;
    private final int rowIndex;

    public RowSkippedEvent(ReadingContext readingContext, int rowIndex) {
        this.readingContext = readingContext;
        this.rowIndex = rowIndex;
    }

    public ReadingContext getReadingContext() {
        return readingContext;
    }

    public int getRowIndex() {
        return rowIndex;
    }
}
