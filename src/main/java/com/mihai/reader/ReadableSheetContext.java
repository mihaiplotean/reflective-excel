package com.mihai.reader;

import com.mihai.reader.bean.RootTableBeanNode;
import com.mihai.reader.deserializer.DeserializationContext;
import com.mihai.reader.exception.BadInputExceptionConsumer;
import com.mihai.reader.table.ReadTable;
import com.mihai.reader.table.TableHeaders;
import com.mihai.reader.table.TableReadingContext;
import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.workbook.sheet.ReadableRow;
import com.mihai.reader.workbook.sheet.ReadableSheet;

import java.util.Iterator;

public class ReadableSheetContext {

    private final ReadableSheet sheet;
    private final TableReadingContext tableReadingContext;
    private final CellReadingContext cellReadingContext;
    private final ReadingContext readingContext;

    public ReadableSheetContext(ReadableSheet sheet, DeserializationContext deserializationContext,
                                BadInputExceptionConsumer exceptionConsumer) {
        this.sheet = sheet;
        this.tableReadingContext = new TableReadingContext();
        this.cellReadingContext = new CellReadingContext();
        this.readingContext = new ReadingContext(sheet, tableReadingContext, cellReadingContext, deserializationContext,
                exceptionConsumer);
    }

    public ReadableSheet getSheet() {
        return sheet;
    }

    public ReadingContext getReadingContext() {
        return readingContext;
    }

    public void setCurrentTableBean(RootTableBeanNode currentTableBean) {
        tableReadingContext.setCurrentBeanNode(currentTableBean);
    }

    public Iterator<ReadableRow> createRowIterator() {
        Iterator<ReadableRow> rowIterator = sheet.iterator();
        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                return rowIterator.hasNext();
            }

            @Override
            public ReadableRow next() {
                ReadableRow row = rowIterator.next();
                cellReadingContext.setCurrentRow(row.getRowNumber());
                return row;
            }
        };
    }

    public Iterator<ReadableRow> createTableRowIterator() {
        int firstTableRow = tableReadingContext.getCurrentTableHeaders().asList().stream()
                .mapToInt(header -> header.getCell().getEndRow())
                .max()
                .orElse(0) + 1;
        Iterator<ReadableRow> rowIterator = createRowIterator(firstTableRow);
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return rowIterator.hasNext();
            }

            @Override
            public ReadableRow next() {
                return tableReadingContext.boundToCurrentTable(rowIterator.next());
            }
        };
    }

    private Iterator<ReadableRow> createRowIterator(int startingRow) {
        Iterator<ReadableRow> rowIterator = createRowIterator();
        int currentRowNumber = 0;
        while (rowIterator.hasNext() && currentRowNumber < startingRow) {
            ReadableRow row = rowIterator.next();
            currentRowNumber = row.getRowNumber() + 1;
        }
        return rowIterator;
    }

    public Iterator<ReadableCell> createCellIterator(ReadableRow row) {
        Iterator<ReadableCell> cellIterator = row.iterator();
        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                return cellIterator.hasNext();
            }

            @Override
            public ReadableCell next() {
                ReadableCell cell = cellIterator.next();
                cellReadingContext.setCurrentColumn(cell.getColumnNumber());
                return cell;
            }
        };
    }

    public ReadableRow getCurrentRow() {
        return readingContext.getCurrentRow();
    }

    public int getCurrentRowNumber() {
        return readingContext.getCurrentRowNumber();
    }

    public int getCurrentColumnNumber() {
        return readingContext.getCurrentColumnNumber();
    }

    public void setReadingTable(boolean readingTable) {
        tableReadingContext.setReadingTable(readingTable);
    }

    public void setCurrentTableHeaders(TableHeaders tableHeaders) {
        tableReadingContext.setCurrentTableHeaders(tableHeaders);
    }

    public void appendTable(ReadTable table) {
        tableReadingContext.appendTable(table);
    }

    public void setCurrentRow(int row) {
        cellReadingContext.setCurrentRow(row);
    }

    public void setCurrentColumn(int column) {
        cellReadingContext.setCurrentColumn(column);
    }

    public void resetCellPointer() {
        cellReadingContext.reset();
    }

    public <T> T getCellValue(String cellValueReference, Class<T> fieldType) {
        return readingContext.getCellValue(cellValueReference, fieldType);
    }

    public String getCellValue(String cellValueReference) {
        return readingContext.getCellValue(cellValueReference);
    }

    public <T> T getCurrentCellValue(Class<T> fieldType) {
        return readingContext.getCurrentCellValue(fieldType);
    }
}
