package com.mihai.integration.fixedcolumns;

import static com.mihai.core.utils.DateFormatUtils.createDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.mihai.assertion.ExcelAssert;
import com.mihai.core.annotation.ExcelColumn;
import com.mihai.reader.ExcelReadingSettings;
import com.mihai.reader.ReflectiveExcelReader;
import com.mihai.reader.deserializer.CellDeserializers;
import com.mihai.writer.ExcelWritingSettings;
import com.mihai.writer.ReflectiveExcelWriter;
import com.mihai.writer.style.StyleProviders;
import com.mihai.writer.style.WritableCellStyles;
import org.junit.jupiter.api.Test;

public class FixedColumnsTest {

    private static final List<TodoRow> EXCEL_ROWS = List.of(
            new TodoRow("buy milk", createDate(12, 12, 2023), TodoRow.TodoPriority.HIGH),
            new TodoRow("go to school", createDate(13, 12, 2023), TodoRow.TodoPriority.MEDIUM),
            new TodoRow("write this library", createDate(14, 3, 2024), TodoRow.TodoPriority.LOW)
    );

    @Test
    public void readingTableWithFixedColumnsReturnsExpectedRows() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/test-fixed-columns.xlsx")) {
            ReflectiveExcelReader reader = new ReflectiveExcelReader(inputStream, ExcelReadingSettings.builder()
                    .registerDeserializer(TodoRow.TodoPriority.class, CellDeserializers.forEnum(TodoRow.TodoPriority.class))
                    .build());
            List<TodoRow> actualRows = reader.readRows(TodoRow.class);
            assertEquals(EXCEL_ROWS, actualRows);
        }
    }

    @Test
    public void writingTableWithFixedColumnsGeneratesExpectedExcelFile() throws IOException {
        File actualFile = File.createTempFile("reflective-excel-writer", "test-fixed-columns.xlsx");

        ReflectiveExcelWriter writer = new ReflectiveExcelWriter(actualFile, ExcelWritingSettings.builder()
                .headerStyleProvider(StyleProviders.of(WritableCellStyles.boldText()))
                .build());
        writer.writeRows(EXCEL_ROWS, TodoRow.class);

        try (InputStream expectedInputStream = getClass().getResourceAsStream("/test-fixed-columns.xlsx")) {
            ExcelAssert.assertThat(actualFile)
                    .isEqualTo(expectedInputStream);
        } finally {
            actualFile.delete();
        }
    }

    public static class TodoRow {

        @ExcelColumn("Description")
        private String description;
        @ExcelColumn("Priority")
        private TodoPriority priority;
        @ExcelColumn("Deadline")
        private Date dueDate;

        public TodoRow() {
        }

        public TodoRow(String description, Date dueDate, TodoPriority priority) {
            this.description = description;
            this.dueDate = dueDate;
            this.priority = priority;
        }

        public String getDescription() {
            return description;
        }

        public Date getDueDate() {
            return dueDate;
        }

        public TodoPriority getPriority() {
            return priority;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TodoRow todoRow = (TodoRow) o;
            return Objects.equals(description, todoRow.description)
                    && Objects.equals(dueDate, todoRow.dueDate)
                    && priority == todoRow.priority;
        }

        @Override
        public int hashCode() {
            return Objects.hash(description, dueDate, priority);
        }

        public enum TodoPriority {
            LOW("Low"),
            MEDIUM("Medium"),
            HIGH("High"),
            ;

            private final String name;

            TodoPriority(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return name;
            }
        }
    }
}
