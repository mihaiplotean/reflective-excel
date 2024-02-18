package com.mihai.writer.style;

import com.mihai.writer.serializer.WritableCellStyle;

public class StyleProviders {

    private StyleProviders() {
        throw new IllegalStateException("Utility class");
    }

    public static StyleProvider forDate() {
        return (context, value) -> new WritableCellStyle("dd/MM/yyy");
    }
}
