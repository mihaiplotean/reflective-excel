package com.mihai.integration.fixedcolumns;

import com.mihai.assertion.ExcelAssert;
import com.mihai.reader.ReflectiveExcelReader;
import com.mihai.reader.deserializer.CellDeserializers;
import com.mihai.writer.ReflectiveExcelWriter;
import com.mihai.writer.style.StyleProviders;
import com.mihai.writer.style.WritableCellStyles;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FixedColumnsTest {

    public static final List<TodoRow> EXCEL_ROWS = List.of(
            new TodoRow("buy milk", createDate(12, 12, 2023), TodoPriority.HIGH),
            new TodoRow("go to school", createDate(13, 12, 2023), TodoPriority.MEDIUM),
            new TodoRow("write this library", createDate(14, 3, 2024), TodoPriority.LOW)
    );

    @Test
    public void simpleReadTest() throws IOException {
        runReadTest("/test-todos.xlsx",
                reader -> reader.registerDeserializer(TodoPriority.class, CellDeserializers.forEnum(TodoPriority.class)),
                EXCEL_ROWS,
                TodoRow.class);
    }

    private <T> void runReadTest(String fileName, Consumer<ReflectiveExcelReader> customizer, List<T> expectedRows, Class<T> clazz)
            throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(fileName)) {
            ReflectiveExcelReader reader = new ReflectiveExcelReader(inputStream);
            customizer.accept(reader);
            List<T> actualRows = reader.readRows(clazz);
            assertEquals(expectedRows, actualRows);
        }
    }

    private static Date createDate(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    @Test
    public void simpleWriteTest() throws IOException {
        runWriteTest("test-todos.xlsx", writer -> {
            writer.setHeaderStyleProvider(StyleProviders.of(WritableCellStyles.boldText()));
            writer.writeRows(EXCEL_ROWS, TodoRow.class);
        });
    }

    private void runWriteTest(String fileName, Consumer<ReflectiveExcelWriter> customizer) throws IOException {
        File actualFile = File.createTempFile("reflective-excel-writer", fileName);

        ReflectiveExcelWriter writer = new ReflectiveExcelWriter(actualFile);
        customizer.accept(writer);

        try (InputStream expectedInputStream = getClass().getResourceAsStream("/" + fileName)) {
            ExcelAssert.assertThat(actualFile)
                    .isEqualTo(expectedInputStream);
            actualFile.delete();
        }
    }
}
