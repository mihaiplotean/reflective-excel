package com.mihai.writer.style;

import com.mihai.writer.WritingContext;
import com.mihai.writer.serializer.WritableCellStyle;

public interface StyleProvider {

    WritableCellStyle getStyle(WritingContext context, Object value);
}
