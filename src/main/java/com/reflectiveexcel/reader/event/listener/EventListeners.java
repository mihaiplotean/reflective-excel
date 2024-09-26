package com.reflectiveexcel.reader.event.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventListeners {

    private final List<TableReadListener> tableReadListeners = new ArrayList<>();
    private final List<HeaderReadListener> headerReadListeners = new ArrayList<>();
    private final List<RowReadListener> rowReadListeners = new ArrayList<>();

    public List<TableReadListener> getTableReadListeners() {
        return Collections.unmodifiableList(tableReadListeners);
    }

    public List<HeaderReadListener> getHeaderReadListeners() {
        return Collections.unmodifiableList(headerReadListeners);
    }

    public List<RowReadListener> getRowReadListeners() {
        return Collections.unmodifiableList(rowReadListeners);
    }

    public void addTableReadListeners(TableReadListener listener) {
        tableReadListeners.add(listener);
    }

    public void addHeaderReadListener(HeaderReadListener listener) {
        headerReadListeners.add(listener);
    }

    public void addRowReadListener(RowReadListener listener) {
        rowReadListeners.add(listener);
    }
}
