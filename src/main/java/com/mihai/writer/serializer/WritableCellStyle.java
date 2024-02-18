package com.mihai.writer.serializer;

public class WritableCellStyle {

    private final String format;

    public WritableCellStyle(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
