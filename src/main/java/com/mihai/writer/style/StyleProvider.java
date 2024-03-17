package com.mihai.writer.style;

import com.mihai.writer.WritingContext;

public interface StyleProvider {

    WritableCellStyle getStyle(WritingContext context, Object target);

    default StyleProvider andThen(StyleProvider other) {
        return (context, target) -> StyleProvider.this.getStyle(context, target)
                .combineWith(other.getStyle(context, target));
    }
}
