package com.reflectiveexcel.writer.sizing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import com.reflectiveexcel.core.annotation.DynamicColumns;
import com.reflectiveexcel.core.annotation.ExcelColumn;
import com.reflectiveexcel.writer.ExcelWritingTest;
import com.reflectiveexcel.writer.annotation.ColumnSize;
import com.reflectiveexcel.writer.node.ChildBeanWriteNode;
import com.reflectiveexcel.writer.node.RootTableBeanWriteNode;
import org.junit.jupiter.api.Test;

public class ColumnSizeSetterTest extends ExcelWritingTest {

    @Test
    public void preferredSizeAppliedToFixedColumn() {
        ColumnSizeSetter columnSizeSetter = new ColumnSizeSetter(getWritableSheet());

        RootTableBeanWriteNode rootNode = new RootTableBeanWriteNode(FixedColumnPreferredTestRow.class, new FixedColumnPreferredTestRow());
        columnSizeSetter.setColumnSizes(Map.of(rootNode.getChildren().get(0), 0));

        assertEquals(20, getSheet().getColumnWidth(0) / 256);
    }

    @Test
    public void preferredSizeAppliedToDynamicColumns() {
        ColumnSizeSetter columnSizeSetter = new ColumnSizeSetter(getWritableSheet());

        RootTableBeanWriteNode rootNode = new RootTableBeanWriteNode(DynamicColumnPreferredTestRow.class,
                                                                     new DynamicColumnPreferredTestRow());
        ChildBeanWriteNode dynamicNode = rootNode.getChildren().get(0);
        List<? extends ChildBeanWriteNode> dynamicLeafNodes = dynamicNode.getChildren();
        columnSizeSetter.setColumnSizes(Map.of(dynamicLeafNodes.get(0), 0, dynamicLeafNodes.get(1), 1));

        assertEquals(20, getSheet().getColumnWidth(0) / 256);
        assertEquals(20, getSheet().getColumnWidth(1) / 256);
    }

    @Test
    public void minSizeAppliedToFixedColumn() {
        ColumnSizeSetter columnSizeSetter = new ColumnSizeSetter(getWritableSheet());

        RootTableBeanWriteNode rootNode = new RootTableBeanWriteNode(FixedColumnMinTestRow.class, new FixedColumnMinTestRow());
        columnSizeSetter.setColumnSizes(Map.of(rootNode.getChildren().get(0), 0));

        assertEquals(42, getSheet().getColumnWidth(0) / 256);
    }

    @Test
    public void minSizeAppliedToDynamicColumns() {
        ColumnSizeSetter columnSizeSetter = new ColumnSizeSetter(getWritableSheet());

        RootTableBeanWriteNode rootNode = new RootTableBeanWriteNode(DynamicColumnMinTestRow.class,
                                                                     new DynamicColumnMinTestRow());
        ChildBeanWriteNode dynamicNode = rootNode.getChildren().get(0);
        List<? extends ChildBeanWriteNode> dynamicLeafNodes = dynamicNode.getChildren();
        columnSizeSetter.setColumnSizes(Map.of(dynamicLeafNodes.get(0), 0, dynamicLeafNodes.get(1), 1));

        assertEquals(42, getSheet().getColumnWidth(0) / 256);
        assertEquals(42, getSheet().getColumnWidth(1) / 256);
    }

    @Test
    public void maxSizeAppliedToFixedColumn() {
        ColumnSizeSetter columnSizeSetter = new ColumnSizeSetter(getWritableSheet());

        RootTableBeanWriteNode rootNode = new RootTableBeanWriteNode(FixedColumnMaxTestRow.class, new FixedColumnMaxTestRow());
        columnSizeSetter.setColumnSizes(Map.of(rootNode.getChildren().get(0), 0));

        assertEquals(2, getSheet().getColumnWidth(0) / 256);
    }

    @Test
    public void maxSizeAppliedToDynamicColumns() {
        ColumnSizeSetter columnSizeSetter = new ColumnSizeSetter(getWritableSheet());

        RootTableBeanWriteNode rootNode = new RootTableBeanWriteNode(DynamicColumnMaxTestRow.class,
                                                                     new DynamicColumnMaxTestRow());
        ChildBeanWriteNode dynamicNode = rootNode.getChildren().get(0);
        List<? extends ChildBeanWriteNode> dynamicLeafNodes = dynamicNode.getChildren();
        columnSizeSetter.setColumnSizes(Map.of(dynamicLeafNodes.get(0), 0, dynamicLeafNodes.get(1), 1));

        assertEquals(2, getSheet().getColumnWidth(0) / 256);
        assertEquals(2, getSheet().getColumnWidth(1) / 256);
    }

    public class FixedColumnPreferredTestRow {

        @ColumnSize(preferred = 20)
        @ExcelColumn("column A")
        private String valueA;
    }

    public class DynamicColumnPreferredTestRow {

        @ColumnSize(preferred = 20)
        @DynamicColumns
        private Map<String, String> values = Map.of(
                "Column A", "Value A",
                "Column B", "Value B"
        );
    }

    public class FixedColumnMinTestRow {

        @ColumnSize(min = 42)
        @ExcelColumn("column A")
        private String valueA;
    }

    public class DynamicColumnMinTestRow {

        @ColumnSize(min = 42)
        @DynamicColumns
        private Map<String, String> values = Map.of(
                "Column A", "Value A",
                "Column B", "Value B"
        );
    }

    public class FixedColumnMaxTestRow {

        @ColumnSize(max = 2)
        @ExcelColumn("column A")
        private String valueA;
    }

    public class DynamicColumnMaxTestRow {

        @ColumnSize(max = 2)
        @DynamicColumns
        private Map<String, String> values = Map.of(
                "Column A", "Value A",
                "Column B", "Value B"
        );
    }
}
