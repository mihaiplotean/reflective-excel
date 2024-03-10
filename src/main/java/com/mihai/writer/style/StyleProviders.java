package com.mihai.writer.style;

public class StyleProviders {

    private static final StyleProvider NO_STYLE_PROVIDER = (context, target) -> WritableCellStyles.noStyle();

    private StyleProviders() {
        throw new IllegalStateException("Utility class");
    }

    public static StyleProvider noStyle() {
        return NO_STYLE_PROVIDER;
    }

    public static StyleProvider of(WritableCellStyle style) {
        return (context, target) -> style;
    }

}
