package com.mihai.reader.readers;

import com.mihai.reader.*;
import com.mihai.reader.bean.RootTableBeanReadNode;
import com.mihai.reader.detector.TableRowColumnDetector;
import com.mihai.reader.mapping.ColumnFieldMapping;
import com.mihai.reader.mapping.DefaultColumnFieldMapping;
import com.mihai.reader.table.ReadTable;
import com.mihai.reader.table.TableHeaders;
import com.mihai.common.workbook.Bounds;
import com.mihai.reader.workbook.sheet.ReadableRow;
import com.mihai.common.workbook.CellLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TableReader {

    private final ReadableSheetContext sheetContext;
    private final ExcelReadingSettings settings;

    public TableReader(ReadableSheetContext sheetContext, ExcelReadingSettings settings) {
        this.sheetContext = sheetContext;
        this.settings = settings;
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

        TableHeaders tableHeaders = new HeaderReader(sheetContext, settings.getRowColumnDetector()).readHeaders();
        if (!tableHeaders.isValid()) {
            sheetContext.setReadingTable(false);
            return List.of();
        }
        sheetContext.setCurrentTableHeaders(tableHeaders);
        List<T> rows = readRows(tableHeaders, clazz);

        CellLocation topRightTableLocation = tableHeaders.asList().get(0).getRoot().getLocation();
        sheetContext.appendTable(new ReadTable(rootBeanNode.getTableId(), tableHeaders, new Bounds(
                topRightTableLocation.getRow(), topRightTableLocation.getColumn(),
                sheetContext.getCurrentRowNumber(), sheetContext.getCurrentColumnNumber()
        )));
        sheetContext.setReadingTable(false);

        return rows;
    }

    private <T> List<T> readRows(TableHeaders tableHeaders, Class<T> clazz) {
        RowReader rowReader = new RowReader(sheetContext, createColumnFieldMapping(clazz, tableHeaders));
        Iterator<ReadableRow> rowIterator = sheetContext.createTableRowIterator();
        TableRowColumnDetector rowColumnDetector = settings.getRowColumnDetector();
        List<T> rows = new ArrayList<>();
        while (rowIterator.hasNext()) {
            ReadableRow row = rowIterator.next();

            if (rowColumnDetector.isLastRow(sheetContext.getReadingContext(), row)) {
                rows.add(rowReader.readRow(row, clazz));
                break;
            }

            if (rowColumnDetector.shouldSkipRow(sheetContext.getReadingContext(), row)) {
                continue;
            }

            rows.add(rowReader.readRow(row, clazz));
        }
        return rows;
    }

    private ColumnFieldMapping createColumnFieldMapping(Class<?> clazz, TableHeaders tableHeaders) {
        ColumnFieldMapping mapping = new DefaultColumnFieldMapping(sheetContext.getReadingContext(), clazz);
        mapping.create(tableHeaders);
        return mapping;
    }
}
