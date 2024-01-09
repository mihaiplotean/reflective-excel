package com.mihai.exception;

import com.mihai.workbook.sheet.RowCells;

public interface BadInputExceptionConsumer {

    void accept(RowCells row, BadInputException exception);
}
