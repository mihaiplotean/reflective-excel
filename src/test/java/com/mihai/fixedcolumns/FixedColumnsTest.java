package com.mihai.fixedcolumns;

import com.mihai.ReflectiveExcelReader;
import com.mihai.deserializer.CellDeserializers;
import com.mihai.writer.ReflectiveExcelWriter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FixedColumnsTest {

    @Test
    public void simpleReadTest() {
        InputStream inputStream = getClass().getResourceAsStream("/test-todos.xlsx");
        ReflectiveExcelReader reader = new ReflectiveExcelReader(inputStream);
        reader.registerDeserializer(TodoPriority.class, CellDeserializers.forEnum(TodoPriority.class));

        List<TodoRow> rows = reader.readRows(TodoRow.class);

        TodoRow firstRow = rows.get(0);
        assertEquals("buy milk", firstRow.getDescription());
        assertEquals(TodoPriority.HIGH, firstRow.getPriority());
        assertEquals(createDate(12, 12, 2023), firstRow.getDueDate());

        TodoRow secondRow = rows.get(1);
        assertEquals("go to school", secondRow.getDescription());
        assertEquals(TodoPriority.MEDIUM, secondRow.getPriority());
        assertEquals(createDate(13, 12, 2023), secondRow.getDueDate());

        TodoRow thirdRow = rows.get(2);
        assertEquals("write this library", thirdRow.getDescription());
        assertEquals(TodoPriority.LOW, thirdRow.getPriority());
        assertEquals(createDate(14, 3, 2024), thirdRow.getDueDate());
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
        List<TodoRow> rows = List.of(
                new TodoRow("buy milk", createDate(12, 12, 2023), TodoPriority.HIGH),
                new TodoRow("go to school", createDate(13, 12, 2023), TodoPriority.MEDIUM),
                new TodoRow("write this library", createDate(14, 3, 2024), TodoPriority.LOW)
        );
        File tempFile = File.createTempFile("reflective-excel-writer", "simple-todo-test.xlsx");
        ReflectiveExcelWriter writer = new ReflectiveExcelWriter(tempFile);
        writer.writeRows(rows, TodoRow.class);
    }
}
