package com.mihai;

import com.mihai.deserializer.CellDeserializers;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReflectiveExcelReaderTest {

    @Test
    public void simpleTest() {
        File file = new File(getClass().getClassLoader().getResource("test-todos.xlsx").getFile());

        ReflectiveExcelReader reader = new ReflectiveExcelReader(file);
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
}