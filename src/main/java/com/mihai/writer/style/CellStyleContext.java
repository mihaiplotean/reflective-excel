package com.mihai.writer.style;

import com.mihai.writer.WritingContext;

public interface CellStyleContext {

    void setHeaderStyleProvider(StyleProvider style);

    WritableCellStyle getHeaderStyle(WritingContext context, Object headerValue);

    void registerColumnStyleProvider(Class<?> clazz, StyleProvider style);

    <T> WritableCellStyle getColumnStyle(WritingContext context, Class<T> clazz, T value);

    void setRowStyleProvider(StyleProvider style);

    WritableCellStyle getRowStyle(WritingContext context, Object row);
}
