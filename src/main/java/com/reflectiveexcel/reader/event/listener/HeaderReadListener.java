package com.reflectiveexcel.reader.event.listener;

import com.reflectiveexcel.reader.event.HeaderReadEvent;
import com.reflectiveexcel.reader.event.MissingHeadersEvent;
import com.reflectiveexcel.core.annotation.ExcelColumn;

public interface HeaderReadListener {

    /**
     * The provided event is fired when headers specified using {@link ExcelColumn} are not found is the table
     * from the Excel file.
     *
     * @param event event related properties.
     */
    void onMissingHeaders(MissingHeadersEvent event);

    /**
     * The provided event is fired after a table header is read.
     *
     * @param event event related properties.
     */
    void afterHeaderRead(HeaderReadEvent event);
}
