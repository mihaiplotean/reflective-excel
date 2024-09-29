package com.reflectiveexcel.reader.event.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class used to specify how to handle events fired when a table is read.
 */
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

    /**
     * Adds an event listener for table events.
     *
     * @param listener table events listener.
     */
    public void addTableReadListeners(TableReadListener listener) {
        tableReadListeners.add(listener);
    }

    /**
     * Adds an event listener for table header events.
     *
     * @param listener header events listener.
     */
    public void addHeaderReadListener(HeaderReadListener listener) {
        headerReadListeners.add(listener);
    }

    /**
     * Adds an event listener for table row events.
     *
     * @param listener row events listener.
     */
    public void addRowReadListener(RowReadListener listener) {
        rowReadListeners.add(listener);
    }
}
