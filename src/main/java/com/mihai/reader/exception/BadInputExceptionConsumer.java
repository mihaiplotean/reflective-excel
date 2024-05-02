package com.mihai.reader.exception;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.workbook.sheet.ReadableRow;

public interface BadInputExceptionConsumer {

    void accept(ReadingContext readingContext, BadInputException exception);
}
