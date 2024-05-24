package com.mihai.integration.fixedcolumns;

import com.mihai.assertion.ExcelAssert;
import com.mihai.reader.ExcelReadingSettings;
import com.mihai.reader.ReflectiveExcelReader;
import com.mihai.reader.deserializer.CellDeserializers;
import com.mihai.writer.ExcelWritingSettings;
import com.mihai.writer.ReflectiveExcelWriter;
import com.mihai.writer.style.StyleProviders;
import com.mihai.writer.style.WritableCellStyles;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.mihai.common.utils.DateFormatUtils.createDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FixedColumnsTest {

    private static final List<TodoRow> EXCEL_ROWS = List.of(
            new TodoRow("buy milk", createDate(12, 12, 2023), TodoPriority.HIGH),
            new TodoRow("go to school", createDate(13, 12, 2023), TodoPriority.MEDIUM),
            new TodoRow("write this library", createDate(14, 3, 2024), TodoPriority.LOW)
    );

    @Test
    public void simpleReadTest() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/test-todos.xlsx")) {
            ReflectiveExcelReader reader = new ReflectiveExcelReader(inputStream, ExcelReadingSettings.builder()
                    .registerDeserializer(TodoPriority.class, CellDeserializers.forEnum(TodoPriority.class))
                    .build());
            List<TodoRow> actualRows = reader.readRows(TodoRow.class);
            assertEquals(EXCEL_ROWS, actualRows);
        }
    }

    @Test
    public void simpleWriteTest() throws IOException {
        File actualFile = File.createTempFile("reflective-excel-writer", "test-todos.xlsx");

        ReflectiveExcelWriter writer = new ReflectiveExcelWriter(actualFile, ExcelWritingSettings.builder()
                .headerStyleProvider(StyleProviders.of(WritableCellStyles.boldText()))
                .build());
        writer.writeRows(EXCEL_ROWS, TodoRow.class);

        try (InputStream expectedInputStream = getClass().getResourceAsStream("/" + "test-todos.xlsx")) {
            ExcelAssert.assertThat(actualFile)
                    .isEqualTo(expectedInputStream);
            actualFile.delete();
        }
    }
}
