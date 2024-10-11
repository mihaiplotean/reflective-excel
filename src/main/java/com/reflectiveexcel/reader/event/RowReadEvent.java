package com.reflectiveexcel.reader.event;

import com.reflectiveexcel.reader.ReadingContext;

public class RowReadEvent {

    private final ReadingContext readingContext;
    private final Object createdRow;

    public RowReadEvent(ReadingContext readingContext, Object createdRow) {
        this.readingContext = readingContext;
        this.createdRow = createdRow;
    }

    public ReadingContext getReadingContext() {
        return readingContext;
    }

    public int getRowIndex() {
        return readingContext.getCurrentRowNumber();
    }

    public Object getCreatedRow() {
        return createdRow;
    }
}
