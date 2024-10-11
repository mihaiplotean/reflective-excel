package com.reflectiveexcel.reader.event.listener;

import com.reflectiveexcel.reader.event.TableReadEvent;

public interface TableReadListener {

    /**
     * The provided event is fired before a table is read.
     *
     * @param event event related properties.
     */
    void beforeTableRead(TableReadEvent event);

    /**
     * The provided event is fired after a table is read.
     *
     * @param event event related properties.
     */
    void afterTableRead(TableReadEvent event);
}
