package com.mihai.writer.style;

import com.mihai.writer.WritingContext;

public interface CellStyleContext {

    void setHeaderStyleProvider(StyleProvider style);

    WritableCellStyle getHeaderStyle(WritingContext context, Object headerValue);

    void setRowStyleProvider(StyleProvider style);

    WritableCellStyle getRowStyle(WritingContext context, Object row);

    void setColumnStyleProvider(StyleProvider style);

    WritableCellStyle getColumnStyle(WritingContext context, Object columnName);

    void registerTypeStyleProvider(Class<?> clazz, WritableCellStyle style);

    <T> WritableCellStyle getTypeStyle(WritingContext context, Class<T> clazz);

    void setCellStyleProvider(StyleProvider style);

    WritableCellStyle getCellStyle(WritingContext context, Object cellValue);
}
