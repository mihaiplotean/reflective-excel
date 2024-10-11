package com.reflectiveexcel.reader.event;

import java.util.List;

import com.reflectiveexcel.reader.ReadingContext;

public class MissingHeadersEvent {

    private final ReadingContext readingContext;
    private final List<String> missingHeaders;

    public MissingHeadersEvent(ReadingContext readingContext, List<String> missingHeaders) {
        this.readingContext = readingContext;
        this.missingHeaders = missingHeaders;
    }

    public ReadingContext getReadingContext() {
        return readingContext;
    }

    public List<String> getMissingHeaders() {
        return missingHeaders;
    }
}
