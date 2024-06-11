package com.mihai.reader.exception;

import com.mihai.reader.ReadingContext;

/**
 * Intercepts the {@link BadInputException} and processes it.
 * This class can be useful, if, for example, you would like to collect all the issues related to the input sheet
 * data, as opposed to instantly throwing the exception when a cell with an unexpected value has been encountered.
 */
public interface BadInputExceptionConsumer {

    void accept(ReadingContext readingContext, BadInputException exception);
}
