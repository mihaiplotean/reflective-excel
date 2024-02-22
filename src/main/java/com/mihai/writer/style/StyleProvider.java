package com.mihai.writer.style;

import com.mihai.writer.WritingContext;

public interface StyleProvider {

    WritableCellStyle getStyle(WritingContext context, Object target);
}
