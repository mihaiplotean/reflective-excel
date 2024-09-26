package com.reflectiveexcel.reader.event.listener;

import com.reflectiveexcel.reader.event.TableReadEvent;

public interface TableReadListener {

    void beforeTableRead(TableReadEvent event);

    void afterTableRead(TableReadEvent event);
}
