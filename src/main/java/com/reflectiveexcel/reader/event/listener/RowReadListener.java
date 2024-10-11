package com.reflectiveexcel.reader.event.listener;

import com.reflectiveexcel.reader.ReadingContext;
import com.reflectiveexcel.reader.detector.TableRowColumnDetector;
import com.reflectiveexcel.reader.event.RowReadEvent;
import com.reflectiveexcel.reader.event.RowSkippedEvent;
import com.reflectiveexcel.reader.workbook.sheet.ReadableRow;

public interface RowReadListener {

    /**
     * The provided event is fired when a row is skipped.
     * Skipped rows are determined by {@link TableRowColumnDetector#shouldSkipRow(ReadingContext, ReadableRow)}.
     *
     * @param event event related properties.
     */
    void onRowSkipped(RowSkippedEvent event);

    /**
     * The provided event is fired after a row is read and the row object created.
     *
     * @param event event related properties.
     */
    void afterRowRead(RowReadEvent event);
}
