package com.mihai.integration.row;

import com.mihai.reader.ExcelReadingSettings;
import com.mihai.reader.ReflectiveExcelReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertiesWithRowsTest {

    @Test
    public void testPropertiesWithRows() {
        InputStream inputStream = getClass().getResourceAsStream("/test-month-expenses.xlsx");

        ExcelReadingSettings settings = ExcelReadingSettings.with()
                .headerStartCellReference("B5")
                .create();

        ReflectiveExcelReader reader = new ReflectiveExcelReader(inputStream);

        FoodExpensesTable table = reader.read(FoodExpensesTable.class, settings);
        assertEquals(7, table.getMonth());
        assertEquals(2021, table.getYear());

        List<FoodExpenseRow> rows = table.getFoodExpenses();

        FoodExpenseRow firstRow = rows.get(0);
        assertEquals("Pasta", firstRow.getName());
        assertEquals(10, firstRow.getPrice());

        FoodExpenseRow secondRow = rows.get(1);
        assertEquals("Rice", secondRow.getName());
        assertEquals(20, secondRow.getPrice());
    }
}
