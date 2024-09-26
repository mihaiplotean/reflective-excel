package com.reflectiveexcel.reader.event.listener;

import com.reflectiveexcel.reader.event.MissingHeadersEvent;

public interface HeaderReadListener {

    void onMissingHeaders(MissingHeadersEvent event);
}
