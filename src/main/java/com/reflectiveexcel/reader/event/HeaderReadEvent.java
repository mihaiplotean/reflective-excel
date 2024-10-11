package com.reflectiveexcel.reader.event;

import com.reflectiveexcel.reader.ReadingContext;
import com.reflectiveexcel.reader.table.TableHeaders;

public class HeaderReadEvent {

    private final ReadingContext readingContext;
    private final TableHeaders headers;

    public HeaderReadEvent(ReadingContext readingContext, TableHeaders headers) {
        this.readingContext = readingContext;
        this.headers = headers;
    }

    public ReadingContext getReadingContext() {
        return readingContext;
    }

    public TableHeaders getHeaders() {
        return headers;
    }
}
