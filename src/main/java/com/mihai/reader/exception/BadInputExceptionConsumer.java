package com.mihai.reader.exception;

import com.mihai.reader.workbook.sheet.ReadableRow;

public interface BadInputExceptionConsumer {

    void accept(ReadableRow row, BadInputException exception);
}
