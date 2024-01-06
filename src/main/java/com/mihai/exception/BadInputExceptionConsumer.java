package com.mihai.exception;

import com.mihai.exception.BadInputException;
import com.mihai.workbook.RowCells;

public interface BadInputExceptionConsumer {

    void accept(RowCells row, BadInputException exception);
}
