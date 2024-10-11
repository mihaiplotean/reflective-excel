package com.reflectiveexcel.reader.readers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.reflectiveexcel.core.annotation.ExcelColumn;
import com.reflectiveexcel.core.annotation.TableId;
import com.reflectiveexcel.core.workbook.Bounds;
import com.reflectiveexcel.reader.ExcelReadingSettings;
import com.reflectiveexcel.reader.ExcelReadingTest;
import com.reflectiveexcel.reader.ReadableSheetContext;
import com.reflectiveexcel.reader.ReadingContext;
import com.reflectiveexcel.reader.detector.SimpleRowColumnDetector;
import com.reflectiveexcel.reader.event.HeaderReadEvent;
import com.reflectiveexcel.reader.event.MissingHeadersEvent;
import com.reflectiveexcel.reader.event.RowReadEvent;
import com.reflectiveexcel.reader.event.RowSkippedEvent;
import com.reflectiveexcel.reader.event.TableReadEvent;
import com.reflectiveexcel.reader.event.listener.EventListeners;
import com.reflectiveexcel.reader.event.listener.HeaderReadListener;
import com.reflectiveexcel.reader.event.listener.RowReadListener;
import com.reflectiveexcel.reader.event.listener.TableReadListener;
import com.reflectiveexcel.reader.table.ReadTable;
import com.reflectiveexcel.reader.table.TableHeaders;
import com.reflectiveexcel.reader.workbook.sheet.ReadableRow;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TableReaderTest extends ExcelReadingTest {

    private static final List<TestRow> EXPECTED_ROWS = List.of(
            new TestRow("value A", "value B"),
            new TestRow("value C", "value D")
    );

    @Test
    public void readSimpleTable() {
        Row row1 = createRow(0);
        row1.createCell(0).setCellValue("Column A");
        row1.createCell(1).setCellValue("Column B");
        Row row2 = createRow(1);
        row2.createCell(0).setCellValue("value A");
        row2.createCell(1).setCellValue("value B");
        Row row3 = createRow(2);
        row3.createCell(0).setCellValue("value C");
        row3.createCell(1).setCellValue("value D");

        ReadableSheetContext sheetContext = createSheetContext();
        TableReader tableReader = new TableReader(sheetContext, ExcelReadingSettings.DEFAULT);
        List<TestRow> testRows = tableReader.readRows(TestRow.class);

        assertEquals(EXPECTED_ROWS, testRows);
        ReadTable readTable = sheetContext.getReadingContext().getLastReadTable();
        assertEquals("test-table", readTable.getId());
        TableHeaders readHeaders = readTable.getHeaders();
        assertEquals("Column A", readHeaders.getHeader(0).getValue());
        assertEquals("Column B", readHeaders.getHeader(1).getValue());
        assertEquals(new Bounds(0, 0, 2, 1), readTable.getBounds());
    }

    @Test
    public void noRowsReadWhenAllRowsSkipped() {
        Row row1 = createRow(0);
        row1.createCell(0).setCellValue("Column A");
        row1.createCell(1).setCellValue("Column B");
        Row row2 = createRow(1);
        row2.createCell(0).setCellValue("value A");
        row2.createCell(1).setCellValue("value B");
        Row row3 = createRow(2);
        row3.createCell(0).setCellValue("value C");
        row3.createCell(1).setCellValue("value D");

        ExcelReadingSettings skipAllRowsSettings = ExcelReadingSettings.builder()
                .rowColumnDetector(new SimpleRowColumnDetector("A1") {
                    @Override
                    public boolean shouldSkipRow(ReadingContext context, ReadableRow tableRow) {
                        return true;
                    }
                })
                .build();
        ReadableSheetContext sheetContext = createSheetContext();
        TableReader tableReader = new TableReader(sheetContext, skipAllRowsSettings);
        List<TestRow> testRows = tableReader.readRows(TestRow.class);

        assertEquals(List.of(), testRows);
    }

    @Test
    public void emptyListReturnedWhenNoRowsToRead() {
        Row headerRow = createRow(0);
        headerRow.createCell(0).setCellValue("Column A");
        headerRow.createCell(1).setCellValue("Column B");

        ReadableSheetContext sheetContext = createSheetContext();
        TableReader tableReader = new TableReader(sheetContext, ExcelReadingSettings.DEFAULT);
        List<TestRow> testRows = tableReader.readRows(TestRow.class);

        assertEquals(List.of(), testRows);
    }

    @Test
    public void noRowsReadWhenNoHeadersFound() {
        ReadableSheetContext sheetContext = createSheetContext();
        TableReader tableReader = new TableReader(sheetContext, ExcelReadingSettings.DEFAULT);
        List<TestRow> testRows = tableReader.readRows(TestRow.class);

        assertEquals(List.of(), testRows);
    }

    @Test
    public void whenHeaderMissingEventFired() {
        createRow(0).createCell(0).setCellValue("Column A");

        List<String> missingHeaderNames = new ArrayList<>();
        EventListeners eventListeners = new EventListeners();
        eventListeners.addHeaderReadListener(new HeaderReadListener() {
            @Override
            public void onMissingHeaders(MissingHeadersEvent event) {
                missingHeaderNames.addAll(event.getMissingHeaders());
            }

            @Override
            public void afterHeaderRead(HeaderReadEvent event) {
                // do nothing
            }
        });
        ExcelReadingSettings settings = ExcelReadingSettings.builder()
                .eventListener(eventListeners)
                .build();

        TableReader tableReader = new TableReader(createSheetContext(), settings);
        tableReader.readRows(TestRow.class);
        assertEquals(List.of("Column B"), missingHeaderNames);
    }

    @Test
    public void whenHeaderReadEventFired() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("Column A");
        row.createCell(1).setCellValue("Unknown");

        EventListeners eventListeners = new EventListeners();
        eventListeners.addHeaderReadListener(new HeaderReadListener() {
            @Override
            public void onMissingHeaders(MissingHeadersEvent event) {
                // do nothing
            }

            @Override
            public void afterHeaderRead(HeaderReadEvent event) {
                TableHeaders readHeaders = event.getHeaders();
                assertNotNull(readHeaders);
                assertEquals("Column A", readHeaders.getHeader(0).getValue());
                assertEquals("Unknown", readHeaders.getHeader(1).getValue());
            }
        });
        ExcelReadingSettings settings = ExcelReadingSettings.builder()
                .eventListener(eventListeners)
                .build();

        TableReader tableReader = new TableReader(createSheetContext(), settings);
        tableReader.readRows(TestRow.class);
    }

    @Test
    public void whenAllHeadersPresentNoEventFired() {
        Row row = createRow(0);
        row.createCell(0).setCellValue("Column A");
        row.createCell(1).setCellValue("Column B");

        EventListeners eventListeners = new EventListeners();
        eventListeners.addHeaderReadListener(new HeaderReadListener() {
            @Override
            public void onMissingHeaders(MissingHeadersEvent event) {
                Assertions.fail("Should not be reached");
            }

            @Override
            public void afterHeaderRead(HeaderReadEvent event) {
                // do nothing
            }
        });
        ExcelReadingSettings settings = ExcelReadingSettings.builder()
                .eventListener(eventListeners)
                .build();

        TableReader tableReader = new TableReader(createSheetContext(), settings);
        tableReader.readRows(TestRow.class);
    }

    @Test
    public void whenRowReadEventFired() {
        Row row1 = createRow(0);
        row1.createCell(0).setCellValue("Column A");
        row1.createCell(1).setCellValue("Column B");
        Row row2 = createRow(1);
        row2.createCell(0).setCellValue("value A");
        row2.createCell(1).setCellValue("value B");

        List<TestRow> readRows = new ArrayList<>();
        List<Integer> readRowIndices = new ArrayList<>();

        EventListeners eventListeners = new EventListeners();
        eventListeners.addRowReadListener(new RowReadListener() {
            @Override
            public void onRowSkipped(RowSkippedEvent event) {
                // do nothing
            }

            @Override
            public void afterRowRead(RowReadEvent event) {
                readRows.add((TestRow) event.getCreatedRow());
                readRowIndices.add(event.getRowIndex());
            }
        });
        ExcelReadingSettings settings = ExcelReadingSettings.builder()
                .eventListener(eventListeners)
                .build();

        TableReader tableReader = new TableReader(createSheetContext(), settings);
        tableReader.readRows(TestRow.class);

        assertEquals(List.of(new TestRow("value A", "value B")), readRows);
        assertEquals(List.of(1), readRowIndices);
    }

    @Test
    public void whenRowSkippedEventFired() {
        Row row1 = createRow(0);
        row1.createCell(0).setCellValue("Column A");
        row1.createCell(1).setCellValue("Column B");
        Row row2 = createRow(1);
        row2.createCell(0).setCellValue("value A");
        row2.createCell(1).setCellValue("value B");
        Row row3 = createRow(2);
        row3.createCell(0).setCellValue("value C");
        row3.createCell(1).setCellValue("value D");

        List<Integer> skippedRows = new ArrayList<>();

        EventListeners eventListeners = new EventListeners();
        eventListeners.addRowReadListener(new RowReadListener() {
            @Override
            public void onRowSkipped(RowSkippedEvent event) {
                skippedRows.add(event.getRowIndex());
            }

            @Override
            public void afterRowRead(RowReadEvent event) {
                Assertions.fail("Should not be reached as all rows are skipped");
            }
        });
        ExcelReadingSettings settings = ExcelReadingSettings.builder()
                .eventListener(eventListeners)
                .rowColumnDetector(new SimpleRowColumnDetector("A1") {
                    @Override
                    public boolean shouldSkipRow(ReadingContext context, ReadableRow tableRow) {
                        return true;
                    }
                })
                .build();

        TableReader tableReader = new TableReader(createSheetContext(), settings);
        tableReader.readRows(TestRow.class);

        assertEquals(List.of(1, 2), skippedRows);
    }

    @Test
    public void beforeTableReadEventFired() {
        Row row1 = createRow(0);
        row1.createCell(0).setCellValue("Column A");
        row1.createCell(1).setCellValue("Column B");
        Row row2 = createRow(1);
        row2.createCell(0).setCellValue("value A");
        row2.createCell(1).setCellValue("value B");

        List<String> readTableIds = new ArrayList<>();
        EventListeners eventListeners = new EventListeners();
        eventListeners.addTableReadListeners(new TableReadListener() {
            @Override
            public void beforeTableRead(TableReadEvent event) {
                readTableIds.add(event.getTableId());
            }

            @Override
            public void afterTableRead(TableReadEvent event) {
                // do nothing
            }
        });
        ExcelReadingSettings settings = ExcelReadingSettings.builder()
                .eventListener(eventListeners)
                .build();

        TableReader tableReader = new TableReader(createSheetContext(), settings);
        tableReader.readRows(TestRow.class);

        assertEquals(List.of("test-table"), readTableIds);
    }

    @Test
    public void afterTableReadEventFired() {
        Row row1 = createRow(0);
        row1.createCell(0).setCellValue("Column A");
        row1.createCell(1).setCellValue("Column B");
        Row row2 = createRow(1);
        row2.createCell(0).setCellValue("value A");
        row2.createCell(1).setCellValue("value B");

        List<String> readTableIds = new ArrayList<>();
        EventListeners eventListeners = new EventListeners();
        eventListeners.addTableReadListeners(new TableReadListener() {
            @Override
            public void beforeTableRead(TableReadEvent event) {
                // do nothing
            }

            @Override
            public void afterTableRead(TableReadEvent event) {
                assertNotNull(event.getReadTable());
                readTableIds.add(event.getTableId());
            }
        });
        ExcelReadingSettings settings = ExcelReadingSettings.builder().eventListener(eventListeners).build();

        TableReader tableReader = new TableReader(createSheetContext(), settings);
        tableReader.readRows(TestRow.class);

        assertEquals(List.of("test-table"), readTableIds);
    }

    @TableId("test-table")
    public static class TestRow {

        @ExcelColumn("Column A")
        private String valueA;

        @ExcelColumn("Column B")
        private String valueB;

        public TestRow() {
        }

        public TestRow(String valueA, String valueB) {
            this.valueA = valueA;
            this.valueB = valueB;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            TestRow testRow = (TestRow) object;
            return Objects.equals(valueA, testRow.valueA) && Objects.equals(valueB, testRow.valueB);
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueA, valueB);
        }

        @Override
        public String toString() {
            return "TestRow{" +
                    "valueA='" + valueA + '\'' +
                    ", valueB='" + valueB + '\'' +
                    '}';
        }
    }
}
