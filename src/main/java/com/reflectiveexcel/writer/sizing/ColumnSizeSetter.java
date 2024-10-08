package com.reflectiveexcel.writer.sizing;

import java.util.Map;
import java.util.Map.Entry;

import com.reflectiveexcel.writer.WritableSheet;
import com.reflectiveexcel.writer.node.ChildBeanWriteNode;
import com.reflectiveexcel.writer.node.ColumnSizePreferences;

public class ColumnSizeSetter {

    private static final int SIZE_UNIT_FACTOR = 256;

    private final WritableSheet sheet;

    public ColumnSizeSetter(WritableSheet sheet) {
        this.sheet = sheet;
    }

    public void setColumnSizes(Map<ChildBeanWriteNode, Integer> leafNodeToColumnIndexMap) {
        for (Entry<ChildBeanWriteNode, Integer> entry : leafNodeToColumnIndexMap.entrySet()) {
            ChildBeanWriteNode node = entry.getKey();
            Integer columnIndex = entry.getValue();
            setColumnSize(node.getColumnSize(), columnIndex);
        }
    }

    private void setColumnSize(ColumnSizePreferences columnSize, int columnIndex) {
        int preferredSize = columnSize.getPreferredSize() * SIZE_UNIT_FACTOR;
        int maxSize = columnSize.getMaxSize() * SIZE_UNIT_FACTOR;
        int minSize = columnSize.getMinSize() * SIZE_UNIT_FACTOR;
        if(preferredSize > 0) {
            int columnWidth = Math.min(Math.max(preferredSize, minSize), maxSize);
            sheet.setColumnWidth(columnIndex, columnWidth);
        }
        else {
            int columnWidth = sheet.autoSizeColumnWidth(columnIndex);
            if(columnWidth < minSize) {
                sheet.setColumnWidth(columnIndex, minSize);
            }
            else if(columnWidth > maxSize) {
                sheet.setColumnWidth(columnIndex, maxSize);
            }
        }
    }
}
