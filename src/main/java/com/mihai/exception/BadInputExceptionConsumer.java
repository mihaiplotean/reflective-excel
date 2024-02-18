package com.mihai.exception;

import com.mihai.workbook.sheet.ReadableRow;

public interface BadInputExceptionConsumer {

    void accept(ReadableRow row, BadInputException exception);
}
