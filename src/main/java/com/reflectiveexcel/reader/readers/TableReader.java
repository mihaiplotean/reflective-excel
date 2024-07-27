package com.reflectiveexcel.reader.readers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.reflectiveexcel.core.workbook.Bounds;
import com.reflectiveexcel.core.workbook.CellLocation;
import com.reflectiveexcel.reader.ExcelReadingSettings;
import com.reflectiveexcel.reader.ReadableSheetContext;
import com.reflectiveexcel.reader.bean.RootTableBeanReadNode;
import com.reflectiveexcel.reader.detector.TableRowColumnDetector;
import com.reflectiveexcel.reader.mapping.ColumnFieldMapping;
import com.reflectiveexcel.reader.mapping.DefaultColumnFieldMapping;
import com.reflectiveexcel.reader.table.ReadTable;
import com.reflectiveexcel.reader.table.TableHeaders;
import com.reflectiveexcel.reader.workbook.sheet.ReadableRow;

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
                topRightTableLocation.row(), topRightTableLocation.column(),
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

            boolean shouldSkipRow = rowColumnDetector.shouldSkipRow(sheetContext.getReadingContext(), row);

            if (rowColumnDetector.isLastRow(sheetContext.getReadingContext(), row)) {
                if (!shouldSkipRow) {
                    rows.add(rowReader.readRow(row, clazz));
                }
                break;
            }

            if (shouldSkipRow) {
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
