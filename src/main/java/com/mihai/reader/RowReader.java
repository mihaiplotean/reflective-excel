package com.mihai.reader;

import java.util.List;

public interface RowReader {

    <T> List<T> readRows(Class<T> clazz);

    <T> List<T> readRows(Class<T> clazz, RowColumnDetector rowColumnDetector);

    ExcelReadingSettings getSettings();
}
