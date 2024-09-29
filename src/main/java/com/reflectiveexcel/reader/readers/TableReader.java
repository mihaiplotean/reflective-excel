package com.reflectiveexcel.reader.readers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.reflectiveexcel.core.workbook.Bounds;
import com.reflectiveexcel.core.workbook.CellLocation;
import com.reflectiveexcel.reader.ExcelReadingSettings;
import com.reflectiveexcel.reader.ReadableSheetContext;
import com.reflectiveexcel.reader.ReadingContext;
import com.reflectiveexcel.reader.bean.ChildBeanReadNode;
import com.reflectiveexcel.reader.bean.RootTableBeanReadNode;
import com.reflectiveexcel.reader.detector.TableRowColumnDetector;
import com.reflectiveexcel.reader.event.HeaderReadEvent;
import com.reflectiveexcel.reader.event.MissingHeadersEvent;
import com.reflectiveexcel.reader.event.RowReadEvent;
import com.reflectiveexcel.reader.event.RowSkippedEvent;
import com.reflectiveexcel.reader.event.TableReadEvent;
import com.reflectiveexcel.reader.event.listener.EventListeners;
import com.reflectiveexcel.reader.mapping.ColumnFieldMapping;
import com.reflectiveexcel.reader.mapping.DefaultColumnFieldMapping;
import com.reflectiveexcel.reader.table.ReadTable;
import com.reflectiveexcel.reader.table.TableHeader;
import com.reflectiveexcel.reader.table.TableHeaders;
import com.reflectiveexcel.reader.workbook.sheet.ReadableRow;

public class TableReader {

    private final ReadableSheetContext sheetContext;
    private final ExcelReadingSettings settings;
    private final EventListeners eventListeners;

    public TableReader(ReadableSheetContext sheetContext, ExcelReadingSettings settings) {
        this.sheetContext = sheetContext;
        this.settings = settings;
        this.eventListeners = settings.getEventListeners();
    }

    public <T> List<T> readRows(Class<T> clazz) {
        RootTableBeanReadNode rootBeanNode = new RootTableBeanReadNode(clazz);
        return readRows(clazz, rootBeanNode);
    }

    public <T> List<T> readRows(Class<T> clazz, String tableId) {
        RootTableBeanReadNode rootBeanNode = new RootTableBeanReadNode(clazz, tableId);
        return readRows(clazz, rootBeanNode);
    }

    private <T> List<T> readRows(Class<T> clazz, RootTableBeanReadNode rootBeanNode) {
        sheetContext.setCurrentTableBean(rootBeanNode);
        sheetContext.setReadingTable(true);
        ReadingContext readingContext = sheetContext.getReadingContext();
        String tableId = rootBeanNode.getTableId();
        fireBeforeTableReadEvent(new TableReadEvent(readingContext, tableId, null));

        TableHeaders tableHeaders = new HeaderReader(sheetContext, settings.getRowColumnDetector()).readHeaders();
        sheetContext.setCurrentTableHeaders(tableHeaders);
        fireAfterHeadersReadEvent(new HeaderReadEvent(sheetContext.getReadingContext(), tableHeaders));

        List<String> missingHeaders = getMissingHeaders(tableHeaders);
        if (!missingHeaders.isEmpty()) {
            fireMissingHeadersEvent(new MissingHeadersEvent(readingContext, missingHeaders));
        }
        if (!tableHeaders.isValid()) {
            sheetContext.setReadingTable(false);
            fireAfterTableReadEvent(new TableReadEvent(readingContext, tableId, null));
            return List.of();
        }
        List<T> rows = readRows(tableHeaders, clazz);

        CellLocation topRightTableLocation = tableHeaders.asList().get(0).getRoot().getLocation();
        ReadTable readTable = new ReadTable(tableId, tableHeaders, new Bounds(
                topRightTableLocation.row(), topRightTableLocation.column(),
                sheetContext.getCurrentRowNumber(), sheetContext.getCurrentColumnNumber()
        ));
        sheetContext.appendTable(readTable);
        sheetContext.setReadingTable(false);
        fireAfterTableReadEvent(new TableReadEvent(readingContext, tableId, readTable));

        return rows;
    }

    private <T> List<T> readRows(TableHeaders tableHeaders, Class<T> clazz) {
        RowReader rowReader = new RowReader(sheetContext, createColumnFieldMapping(clazz, tableHeaders));
        Iterator<ReadableRow> rowIterator = sheetContext.createTableRowIterator();
        TableRowColumnDetector rowColumnDetector = settings.getRowColumnDetector();
        List<T> rows = new ArrayList<>();
        while (rowIterator.hasNext()) {
            ReadableRow row = rowIterator.next();

            boolean shouldSkipRow = rowColumnDetector.shouldSkipRow(sheetContext.getReadingContext(), row);

            if (rowColumnDetector.isLastRow(sheetContext.getReadingContext(), row)) {
                if (!shouldSkipRow) {
                    T createdRow = rowReader.readRow(row, clazz);
                    rows.add(createdRow);
                    fireRowReadEvent(new RowReadEvent(sheetContext.getReadingContext(), createdRow));
                } else {
                    fireRowSkippedEvent(new RowSkippedEvent(sheetContext.getReadingContext(), row.getRowNumber()));
                }
                break;
            }

            if (shouldSkipRow) {
                fireRowSkippedEvent(new RowSkippedEvent(sheetContext.getReadingContext(), row.getRowNumber()));
                continue;
            }

            T createdRow = rowReader.readRow(row, clazz);
            rows.add(createdRow);
            fireRowReadEvent(new RowReadEvent(sheetContext.getReadingContext(), createdRow));
        }
        return rows;
    }

    private ColumnFieldMapping createColumnFieldMapping(Class<?> clazz, TableHeaders tableHeaders) {
        ColumnFieldMapping mapping = new DefaultColumnFieldMapping(sheetContext.getReadingContext(), clazz);
        mapping.create(tableHeaders);
        return mapping;
    }

    private void fireBeforeTableReadEvent(TableReadEvent event) {
        eventListeners.getTableReadListeners().forEach(listener -> listener.beforeTableRead(event));
    }

    private void fireAfterHeadersReadEvent(HeaderReadEvent event) {
        eventListeners.getHeaderReadListeners().forEach(listener -> listener.afterHeaderRead(event));
    }

    private void fireMissingHeadersEvent(MissingHeadersEvent event) {
        eventListeners.getHeaderReadListeners().forEach(listener -> listener.onMissingHeaders(event));
    }

    private void fireRowReadEvent(RowReadEvent event) {
        eventListeners.getRowReadListeners().forEach(listener -> listener.afterRowRead(event));
    }

    private void fireRowSkippedEvent(RowSkippedEvent event) {
        eventListeners.getRowReadListeners().forEach(listener -> listener.onRowSkipped(event));
    }

    private void fireAfterTableReadEvent(TableReadEvent event) {
        eventListeners.getTableReadListeners().forEach(listener -> listener.afterTableRead(event));
    }

    private List<String> getMissingHeaders(TableHeaders headers) {
        List<String> foundHeaders = new ArrayList<>();
        for (TableHeader header : headers) {
            foundHeaders.add(header.getPath());
        }
        List<String> expectedHeaders = new ArrayList<>();
        for (ChildBeanReadNode node : sheetContext.getReadingContext().getCurrentTableBean().getChildren()) {
            expectedHeaders.addAll(getAllLeafPaths(node));
        }
        expectedHeaders.removeAll(foundHeaders);
        return expectedHeaders;
    }

    public List<String> getAllLeafPaths(ChildBeanReadNode node) {
        List<String> paths = new ArrayList<>();
        collectLeafPaths(node, new ArrayList<>(), paths);
        return paths;
    }

    private void collectLeafPaths(ChildBeanReadNode node, List<String> currentPathNodes, List<String> allLeafPaths) {
        currentPathNodes.add(node.getName());
        if (node.getChildren().isEmpty()) {
            allLeafPaths.add(String.join("/", currentPathNodes));
            return;
        }
        for (ChildBeanReadNode child : node.getChildren()) {
            collectLeafPaths(child, new ArrayList<>(currentPathNodes), allLeafPaths);
        }
    }
}
