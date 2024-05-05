package com.mihai.reader.detector;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.workbook.sheet.ReadableRow;

public interface RowColumnDetector {

    boolean isHeaderRow(ReadingContext context, ReadableRow row);

    boolean isHeaderStartColumn(ReadingContext context, ReadableCell cell);

    boolean isHeaderLastColumn(ReadingContext context, ReadableCell cell);

    boolean isLastRow(ReadingContext context, ReadableRow row);

    boolean shouldSkipRow(ReadingContext context, ReadableRow row);
}
