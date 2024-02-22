package com.mihai.writer.style;

public class StyleProviders {

    private static final StyleProvider NO_STYLE_PROVIDER = (context, target) -> WritableCellStyle.builder().build();

    private StyleProviders() {
        throw new IllegalStateException("Utility class");
    }

    public static StyleProvider noStyle() {
        return NO_STYLE_PROVIDER;
    }

    public static StyleProvider forDate() {
        return (context, target) -> WritableCellStyle.withDataFormat("dd/MM/yyy");
    }

    public static StyleProvider of(WritableCellStyle style) {
        return (context, target) -> style;
    }
}
