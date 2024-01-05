package com.mihai;

import java.util.List;

public interface RowReader {

    <T> List<T> readRows(Class<T> clazz);
}
