package com.mihai.writer.style;

import com.mihai.writer.WritingContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DefaultStyleContext implements CellStyleContext {

    private final Map<Class<?>, StyleProvider> styleProviderMap = new HashMap<>();

    private StyleProvider headerStyleProvider = StyleProviders.noStyle();
    private StyleProvider rowStyleProvider = StyleProviders.noStyle();

    public DefaultStyleContext() {
        registerStyleProviders();
    }

    protected void registerStyleProviders() {
        registerColumnStyleProvider(Date.class, StyleProviders.forDate());

        WritableCellStyle boldTextCellBorders = WritableCellStyles.boldText()
                .combineWith(WritableCellStyles.allSideBorder())
                .combineWith(WritableCellStyles.backgroundColor(232, 220, 202));
        setHeaderStyleProvider(StyleProviders.of(boldTextCellBorders));

        setRowStyleProvider(StyleProviders.of(WritableCellStyles.allSideBorder()));
    }

    @Override
    public void setHeaderStyleProvider(StyleProvider style) {
        this.headerStyleProvider = style;
    }

    @Override
    public WritableCellStyle getHeaderStyle(WritingContext context, Object headerValue) {
        return headerStyleProvider.getStyle(context, headerValue);
    }

    @Override
    public void registerColumnStyleProvider(Class<?> clazz, StyleProvider style) {
        styleProviderMap.put(clazz, style);
    }

    @Override
    public <T> WritableCellStyle getColumnStyle(WritingContext context, Class<T> clazz, T value) {
        StyleProvider styleProvider = styleProviderMap.getOrDefault(clazz, StyleProviders.noStyle());
        return styleProvider.getStyle(context, value);
    }

    @Override
    public void setRowStyleProvider(StyleProvider style) {
        this.rowStyleProvider = style;
    }

    @Override
    public WritableCellStyle getRowStyle(WritingContext context, Object row) {
        return rowStyleProvider.getStyle(context, row);
    }
}
