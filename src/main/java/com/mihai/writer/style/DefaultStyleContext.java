package com.mihai.writer.style;

import com.mihai.writer.WritingContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DefaultStyleContext implements CellStyleContext {

    private final Map<Class<?>, WritableCellStyle> typeStyleProviderMap = new HashMap<>();

    private StyleProvider headerStyleProvider = StyleProviders.noStyle();
    private StyleProvider rowStyleProvider = StyleProviders.noStyle();
    private StyleProvider columnStyleProvider = StyleProviders.noStyle();
    private StyleProvider celltyleProvider = StyleProviders.noStyle();

    public DefaultStyleContext() {
        registerStyleProviders();
    }

    protected void registerStyleProviders() {
        registerTypeStyleProvider(Date.class, WritableCellStyles.forDate());
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
    public void registerTypeStyleProvider(Class<?> clazz, WritableCellStyle style) {
        typeStyleProviderMap.put(clazz, style);
    }

    @Override
    public <T> WritableCellStyle getTypeStyle(WritingContext context, Class<T> clazz) {
        return typeStyleProviderMap.getOrDefault(clazz, WritableCellStyles.noStyle());
    }

    @Override
    public void setRowStyleProvider(StyleProvider style) {
        this.rowStyleProvider = style;
    }

    @Override
    public WritableCellStyle getRowStyle(WritingContext context, Object row) {
        return rowStyleProvider.getStyle(context, row);
    }

    @Override
    public void setColumnStyleProvider(StyleProvider style) {
        this.columnStyleProvider = style;
    }

    @Override
    public WritableCellStyle getColumnStyle(WritingContext context, Object columnName) {
        return columnStyleProvider.getStyle(context, columnName);
    }

    @Override
    public void setCellStyleProvider(StyleProvider style) {
        this.celltyleProvider = style;
    }

    @Override
    public WritableCellStyle getCellStyle(WritingContext context, Object cellValue) {
        return celltyleProvider.getStyle(context, context);
    }
}
