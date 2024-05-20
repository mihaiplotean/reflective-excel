package com.mihai.common;

public class ReflectiveExcelException extends RuntimeException {

    public ReflectiveExcelException(String message) {
        super(message);
    }

    public ReflectiveExcelException(String message, Throwable cause) {
        super(message, cause);
    }
}
