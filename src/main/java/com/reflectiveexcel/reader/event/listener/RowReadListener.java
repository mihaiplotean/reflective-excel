package com.reflectiveexcel.reader.event.listener;

import com.reflectiveexcel.reader.event.RowReadEvent;
import com.reflectiveexcel.reader.event.RowSkippedEvent;

public interface RowReadListener {

    void onRowSkipped(RowSkippedEvent event);

    void afterRowRead(RowReadEvent event);
}
