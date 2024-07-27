package com.reflectiveexcel.integration.fixedcolumns;

import static com.reflectiveexcel.core.utils.DateFormatUtils.createDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.reflectiveexcel.assertion.ExcelAssert;
import com.reflectiveexcel.core.annotation.ExcelColumn;
import com.reflectiveexcel.integration.fixedcolumns.FixedColumnsTest.TodoRow.TodoPriority;
import com.reflectiveexcel.reader.ExcelReadingSettings;
import com.reflectiveexcel.reader.ReflectiveExcelReader;
import com.reflectiveexcel.reader.deserializer.CellDeserializers;
import com.reflectiveexcel.reader.deserializer.DefaultDeserializationContext;
import com.reflectiveexcel.writer.ExcelWritingSettings;
import com.reflectiveexcel.writer.ReflectiveExcelWriter;
import com.reflectiveexcel.writer.style.CellStyleContext;
import com.reflectiveexcel.writer.style.DefaultStyleContext;
import com.reflectiveexcel.writer.style.StyleProviders;
import com.reflectiveexcel.writer.style.WritableCellStyles;
import org.junit.jupiter.api.Test;

public class FixedColumnsTest {

    private static final List<TodoRow> EXCEL_ROWS = List.of(
            new TodoRow("buy milk", createDate(12, 12, 2023), TodoPriority.HIGH),
            new TodoRow("go to school", createDate(13, 12, 2023), TodoPriority.MEDIUM),
            new TodoRow("write this library", createDate(14, 3, 2024), TodoPriority.LOW)
    );

    @Test
    public void readingTableWithFixedColumnsReturnsExpectedRows() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/test-fixed-columns.xlsx")) {
            DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext();
            deserializationContext.registerDeserializer(TodoPriority.class, CellDeserializers.forEnum(TodoPriority.class));

            ReflectiveExcelReader reader = new ReflectiveExcelReader(inputStream, ExcelReadingSettings.builder()
                    .deserializationContext(deserializationContext)
                    .build());
            List<TodoRow> actualRows = reader.readRows(TodoRow.class);
            assertEquals(EXCEL_ROWS, actualRows);
        }
    }

    @Test
    public void writingTableWithFixedColumnsGeneratesExpectedExcelFile() throws IOException {
        File actualFile = File.createTempFile("reflective-excel-writer", "test-fixed-columns.xlsx");

        CellStyleContext styleContext = new DefaultStyleContext();
        styleContext.setHeaderStyleProvider(StyleProviders.of(WritableCellStyles.boldText()));

        ExcelWritingSettings settings = ExcelWritingSettings.builder()
                .cellStyleContext(styleContext)
                .build();

        ReflectiveExcelWriter writer = new ReflectiveExcelWriter(actualFile, settings);
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
