package com.mihai.writer.style;

import com.mihai.writer.WritingContext;
import com.mihai.writer.serializer.WritableCellStyle;

public interface CellStyleContext {

    <T> WritableCellStyle getStyle(WritingContext context, Class<T> clazz, T value);

    void registerStyleProvider(Class<?> clazz, StyleProvider style);
}
