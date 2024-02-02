package com.mihai;

import com.mihai.annotation.ExcelRows;

import java.util.List;

public interface RowReader {

    <T> List<T> readRows(Class<T> clazz);

    <T> List<T> readRows(Class<T> clazz, RowColumnDetector rowColumnDetector);

    ExcelReadingSettings getSettings();
}
