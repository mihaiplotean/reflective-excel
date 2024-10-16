package com.reflectiveexcel.writer.writers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import com.reflectiveexcel.core.annotation.DynamicColumns;
import com.reflectiveexcel.core.annotation.ExcelCellGroup;
import com.reflectiveexcel.core.annotation.ExcelColumn;
import com.reflectiveexcel.reader.ReadingContext;
import com.reflectiveexcel.reader.detector.DynamicColumnDetector;
import com.reflectiveexcel.reader.detector.MaybeDynamicColumn;
import com.reflectiveexcel.writer.ExcelWritingTest;
import com.reflectiveexcel.writer.node.RootTableBeanWriteNode;
import com.reflectiveexcel.writer.table.WrittenTableHeaders;
import org.junit.jupiter.api.Test;

public class HeaderWriterTest extends ExcelWritingTest {

    @Test
    public void fixedColumnHeaderIsWritten() {
        RootTableBeanWriteNode node = new RootTableBeanWriteNode(OneFixedColumn.class, new OneFixedColumn());
        WrittenTableHeaders headers = createHeaderWriter().writeHeaders(node);

        assertEquals("test column A", headers.getColumnName(0));
        assertEquals("test column A", getCell(0, 0).getStringCellValue());
    }

    @Test
    public void inheritedFixedColumnIsWritten() {
        RootTableBeanWriteNode node = new RootTableBeanWriteNode(InheritColumns.class, new InheritColumns());
        WrittenTableHeaders headers = createHeaderWriter().writeHeaders(node);

        assertEquals("test column A", headers.getColumnName(0));
        assertEquals("test column B", headers.getColumnName(1));

        assertEquals("test column A", getCell(0, 0).getStringCellValue());
        assertEquals("test column B", getCell(0, 1).getStringCellValue());
    }

    @Test
    public void dynamicColumnsAreWritten() {
        RootTableBeanWriteNode node = new RootTableBeanWriteNode(DynamicColumnSet.class, new DynamicColumnSet());
        WrittenTableHeaders headers = createHeaderWriter().writeHeaders(node);

        assertEquals("1", headers.getColumnName(0));
        assertEquals("2", headers.getColumnName(1));

        assertEquals(1, getCell(0, 0).getNumericCellValue());
        assertEquals(2, getCell(0, 1).getNumericCellValue());
    }

    @Test
    public void groupedColumnsAreWritten() {
        RootTableBeanWriteNode node = new RootTableBeanWriteNode(GroupedColumns.class, new GroupedColumns());
        WrittenTableHeaders headers = createHeaderWriter().writeHeaders(node);

        assertEquals("Column A", headers.getColumnName(0));
        assertEquals("Column B", headers.getColumnName(1));

        assertEquals("Test group", getCell(0, 0).getStringCellValue());
        assertEquals("Column A", getCell(1, 0).getStringCellValue());
        assertEquals("Column B", getCell(1, 1).getStringCellValue());
    }

    @Test
    public void nestedGroupedColumnsAreWritten() {
        RootTableBeanWriteNode node = new RootTableBeanWriteNode(NestedGroupedColumns.class, new NestedGroupedColumns());
        WrittenTableHeaders headers = createHeaderWriter().writeHeaders(node);

        assertEquals("Outer group column", headers.getColumnName(0));
        assertEquals("Inner group column A", headers.getColumnName(1));
        assertEquals("Inner group column B", headers.getColumnName(2));

        assertEquals("Group", getCell(0, 0).getStringCellValue());

        assertEquals("Outer group column", getCell(1, 0).getStringCellValue());
        assertEquals("Sub-group", getCell(1, 1).getStringCellValue());

        assertEquals("Inner group column A", getCell(2, 1).getStringCellValue());
        assertEquals("Inner group column B", getCell(2, 2).getStringCellValue());
    }

    private static class OneFixedColumn {

        @ExcelColumn(value = "test column A")
        private String column;
    }

    private static class InheritColumns extends OneFixedColumn {

        @ExcelColumn(value = "test column B")
        private String column;
    }

    protected static class DynamicColumnSet {

        @DynamicColumns(detector = NeverDynamicColumnDetector.class)
        private Map<Integer, String> columns = new LinkedHashMap<>();

        public DynamicColumnSet() {
            columns.put(1, "");
            columns.put(2, "");
        }

        protected static class NeverDynamicColumnDetector implements DynamicColumnDetector {

            public NeverDynamicColumnDetector() {
            }

            @Override
            public boolean test(ReadingContext context, MaybeDynamicColumn column) {
                return false;
            }
        }
    }

    private static class GroupedColumns {

        @ExcelCellGroup(value = "Test group")
        private InnerGroup group = new InnerGroup();

        private static class InnerGroup {

            @ExcelColumn(value = "Column A")
            private String columnA;

            @ExcelColumn(value = "Column B")
            private String columnB;
        }
    }

    private static class NestedGroupedColumns {

        @ExcelCellGroup(value = "Group")
        private OuterGroup group = new OuterGroup();

        private static class OuterGroup {

            @ExcelColumn(value = "Outer group column")
            private String value;

            @ExcelCellGroup(value = "Sub-group")
            private InnerGroup group = new InnerGroup();

            private static class InnerGroup {

                @ExcelColumn(value = "Inner group column A")
                private String value;

                @ExcelColumn(value = "Inner group column B")
                private String columnB;
            }
        }
    }
}
