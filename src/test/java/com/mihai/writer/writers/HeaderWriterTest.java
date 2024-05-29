package com.mihai.writer.writers;

import com.mihai.core.annotation.DynamicColumns;
import com.mihai.core.annotation.ExcelCellGroup;
import com.mihai.core.annotation.ExcelColumn;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.detector.ColumnDetector;
import com.mihai.reader.workbook.sheet.ReadableCell;
import com.mihai.writer.WritableSheetContext;
import com.mihai.writer.WritableSheet;
import com.mihai.writer.node.RootTableBeanWriteNode;
import com.mihai.writer.serializer.DefaultSerializationContext;
import com.mihai.writer.style.DefaultStyleContext;
import com.mihai.writer.table.WrittenTableHeaders;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeaderWriterTest {

    private XSSFWorkbook workbook;
    private XSSFSheet actualSheet;
    private HeaderWriter headerWriter;

    @BeforeEach
    public void setUp() {
        workbook = new XSSFWorkbook();
        actualSheet = workbook.createSheet();
        headerWriter = new HeaderWriter(new CellWriter(new WritableSheet(actualSheet)),
                new WritableSheetContext(new DefaultSerializationContext(), new DefaultStyleContext()));
    }

    @AfterEach
    public void tearDown() throws IOException {
        workbook.close();
    }

    @Test
    public void fixedColumnHeaderIsWritten() {
        RootTableBeanWriteNode node = new RootTableBeanWriteNode(OneFixedColumn.class, new OneFixedColumn());
        WrittenTableHeaders headers = headerWriter.writeHeaders(node);

        assertEquals("test column A", headers.getColumnName(0));
        assertEquals("test column A", actualSheet.getRow(0).getCell(0).getStringCellValue());
    }

    @Test
    public void inheritedFixedColumnIsWritten() {
        RootTableBeanWriteNode node = new RootTableBeanWriteNode(InheritColumns.class, new InheritColumns());
        WrittenTableHeaders headers = headerWriter.writeHeaders(node);

        assertEquals("test column A", headers.getColumnName(0));
        assertEquals("test column B", headers.getColumnName(1));

        assertEquals("test column A", actualSheet.getRow(0).getCell(0).getStringCellValue());
        assertEquals("test column B", actualSheet.getRow(0).getCell(1).getStringCellValue());
    }

    @Test
    public void dynamicColumnsAreWritten() {
        RootTableBeanWriteNode node = new RootTableBeanWriteNode(DynamicColumnSet.class, new DynamicColumnSet());
        WrittenTableHeaders headers = headerWriter.writeHeaders(node);

        assertEquals("1", headers.getColumnName(0));
        assertEquals("2", headers.getColumnName(1));

        assertEquals(1, actualSheet.getRow(0).getCell(0).getNumericCellValue());
        assertEquals(2, actualSheet.getRow(0).getCell(1).getNumericCellValue());
    }

    @Test
    public void groupedColumnsAreWritten() {
        RootTableBeanWriteNode node = new RootTableBeanWriteNode(GroupedColumns.class, new GroupedColumns());
        WrittenTableHeaders headers = headerWriter.writeHeaders(node);

        assertEquals("Column A", headers.getColumnName(0));
        assertEquals("Column B", headers.getColumnName(1));

        assertEquals("Test group", actualSheet.getRow(0).getCell(0).getStringCellValue());
        assertEquals("Column A", actualSheet.getRow(1).getCell(0).getStringCellValue());
        assertEquals("Column B", actualSheet.getRow(1).getCell(1).getStringCellValue());
    }

    @Test
    public void nestedGroupedColumnsAreWritten() {
        RootTableBeanWriteNode node = new RootTableBeanWriteNode(NestedGroupedColumns.class, new NestedGroupedColumns());
        WrittenTableHeaders headers = headerWriter.writeHeaders(node);

        assertEquals("Outer group column", headers.getColumnName(0));
        assertEquals("Inner group column A", headers.getColumnName(1));
        assertEquals("Inner group column B", headers.getColumnName(2));

        assertEquals("Group", actualSheet.getRow(0).getCell(0).getStringCellValue());

        assertEquals("Outer group column", actualSheet.getRow(1).getCell(0).getStringCellValue());
        assertEquals("Sub-group", actualSheet.getRow(1).getCell(1).getStringCellValue());

        assertEquals("Inner group column A", actualSheet.getRow(2).getCell(1).getStringCellValue());
        assertEquals("Inner group column B", actualSheet.getRow(2).getCell(2).getStringCellValue());
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

        @DynamicColumns(detector = NeverColumnDetector.class)
        private Map<Integer, String> columns = new LinkedHashMap<>();

        public DynamicColumnSet() {
            columns.put(1, "");
            columns.put(2, "");
        }

        protected static class NeverColumnDetector implements ColumnDetector {

            public NeverColumnDetector() {
            }

            @Override
            public boolean test(ReadingContext context, ReadableCell columnCell) {
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
