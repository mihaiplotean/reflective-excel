package com.reflectiveexcel.reader;

import java.util.Iterator;

import com.reflectiveexcel.core.CellPointer;
import com.reflectiveexcel.reader.bean.RootTableBeanReadNode;
import com.reflectiveexcel.reader.table.ReadTable;
import com.reflectiveexcel.reader.table.TableHeaders;
import com.reflectiveexcel.reader.table.TableReadingContext;
import com.reflectiveexcel.reader.workbook.sheet.ReadableCell;
import com.reflectiveexcel.reader.workbook.sheet.ReadableRow;
import com.reflectiveexcel.reader.workbook.sheet.ReadableSheet;

public class ReadableSheetContext {

    private final ReadableSheet sheet;
    private final TableReadingContext tableReadingContext;
    private final CellPointer cellPointer;
    private final ReadingContext readingContext;

    public ReadableSheetContext(ReadableSheet sheet, ExcelReadingSettings settings) {
        this.sheet = sheet;
        this.tableReadingContext = new TableReadingContext();
        this.cellPointer = new CellPointer();
        this.readingContext = new ReadingContext(sheet, tableReadingContext, cellPointer, settings);
    }

    public ReadableSheet getSheet() {
        return sheet;
    }

    public ReadingContext getReadingContext() {
        return readingContext;
    }

    public void setCurrentTableBean(RootTableBeanReadNode currentTableBean) {
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
                cellPointer.setCurrentRow(row.getRowNumber());
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
                cellPointer.setCurrentColumn(cell.getColumnNumber());
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
        cellPointer.setCurrentRow(row);
    }

    public void setCurrentColumn(int column) {
        cellPointer.setCurrentColumn(column);
    }

    public void resetCellPointer() {
        cellPointer.reset();
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
