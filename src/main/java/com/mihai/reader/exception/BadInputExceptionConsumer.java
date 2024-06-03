package com.mihai.reader.exception;

import com.mihai.reader.ReadingContext;

public interface BadInputExceptionConsumer {

    void accept(ReadingContext readingContext, BadInputException exception);
}
