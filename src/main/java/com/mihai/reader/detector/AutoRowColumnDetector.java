package com.mihai.reader.detector;

import com.mihai.CollectionUtilities;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.bean.ChildBeanNode;
import com.mihai.reader.bean.RootTableBeanNode;
import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.reader.workbook.sheet.ReadableRow;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class AutoRowColumnDetector implements RowColumnDetector2 {

    @Override
    public boolean isLastRow(ReadingContext context, ReadableRow row) {
        return isNextRowEmpty(context, row);
    }

    private static boolean isNextRowEmpty(ReadingContext context, ReadableRow row) {
        return context.getRow(row.getRowNumber() + 1).isEmpty();
    }

    @Override
    public boolean isHeaderRow(ReadingContext context, ReadableRow row) {
        return currentRowHasAllDefinedHeaders(context, row);
    }

    private static boolean currentRowHasAllDefinedHeaders(ReadingContext context, ReadableRow row) {
        RootTableBeanNode currentTableBean = context.getCurrentTableBean();
        List<String> beanDefinedColumnNames = getDefinedColumnsInBean(currentTableBean);
        Set<String> rowValues = row.stream()
                .map(ReadableCell::getValue)
                .collect(CollectionUtilities.toCaseInsensitiveSetCollector());
        return rowValues.containsAll(beanDefinedColumnNames);
    }

    private static List<String> getDefinedColumnsInBean(RootTableBeanNode currentTableBean) {
        return currentTableBean.getChildren().stream()
                .map(ChildBeanNode::getLeaves)
                .flatMap(Collection::stream)
                .map(ChildBeanNode::getName)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public boolean shouldSkipRow(ReadingContext context, ReadableRow row) {
        return false;
    }

    @Override
    public boolean isHeaderStartColumn(ReadingContext context, ReadableCell cell) {
        return columnsInRowHaveAllDefinedHeaders(context, cell);
    }

    private static boolean columnsInRowHaveAllDefinedHeaders(ReadingContext context, ReadableCell cell) {
        RootTableBeanNode currentTableBean = context.getCurrentTableBean();
        Set<String> cellValues = context.getRow(cell.getRowNumber()).getCells().stream()
                .filter(currentCell -> currentCell.getColumnNumber() >= cell.getColumnNumber())
                .map(ReadableCell::getValue)
                .collect(CollectionUtilities.toCaseInsensitiveSetCollector());
        return cellValues.containsAll(getDefinedColumnsInBean(currentTableBean));
    }

    @Override
    public boolean isHeaderLastColumn(ReadingContext context, ReadableCell cell) {
        return isNextColumnEmpty(context, cell);
    }

    private static boolean isNextColumnEmpty(ReadingContext context, ReadableCell cell) {
        int rowNumber = cell.getRowNumber();
        int columnNumber = cell.getColumnNumber();
        return StringUtils.isEmpty(context.getCellValue(rowNumber, columnNumber + 1));
    }
}
