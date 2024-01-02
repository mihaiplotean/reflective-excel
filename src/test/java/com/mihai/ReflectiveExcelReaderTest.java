package com.mihai;

import com.mihai.deserializer.CellDeserializers;
import com.mihai.row.*;
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

    @Test
    public void testDynamicColumns() {
        File file = new File(getClass().getClassLoader().getResource("test-population.xlsx").getFile());

        List<PopulationRow> rows = new ReflectiveExcelReader(file).readRows(PopulationRow.class);

        PopulationRow firstRow = rows.get(0);
        assertEquals(1, firstRow.getId());
        assertEquals("Moldova", firstRow.getCountry());
        assertEquals(120, firstRow.getPopulation(2021));
        assertEquals(130, firstRow.getPopulation(2022));
        assertEquals(200, firstRow.getPopulation(2023));

        PopulationRow secondRow = rows.get(1);
        assertEquals(2, secondRow.getId());
        assertEquals("Netherlands", secondRow.getCountry());
        assertEquals(321, secondRow.getPopulation(2021));
        assertEquals(420, secondRow.getPopulation(2022));
        assertEquals(500, secondRow.getPopulation(2023));
    }

    @Test
    public void testPropertiesWithRows() {
        File file = new File(getClass().getClassLoader().getResource("test-month-expenses.xlsx").getFile());

        ExcelReadingSettings settings = ExcelReadingSettings.with()
                .headerStartCellReference("B5")
                .create();

        ReflectiveExcelReader reader = new ReflectiveExcelReader(file, settings);
        List<FoodExpenseRow> rows = reader.readRows(FoodExpenseRow.class);
        FoodExpenseRow firstRow = rows.get(0);
        assertEquals("Pasta", firstRow.getName());
        assertEquals(10, firstRow.getPrice());

        FoodExpenseRow secondRow = rows.get(1);
        assertEquals("Rice", secondRow.getName());
        assertEquals(20, secondRow.getPrice());

        FoodExpensesProperties properties = reader.read(FoodExpensesProperties.class);
        assertEquals(7, properties.getMonth());
        assertEquals(2021, properties.getYear());
    }
}