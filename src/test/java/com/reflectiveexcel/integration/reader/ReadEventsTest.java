package com.reflectiveexcel.integration.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.util.List;

import com.reflectiveexcel.integration.reader.TableAndPropertiesTest.FoodExpensesTable;
import com.reflectiveexcel.integration.reader.TableAndPropertiesTest.FoodExpensesTable.FoodExpenseRow;
import com.reflectiveexcel.reader.ExcelReadingSettings;
import com.reflectiveexcel.reader.ReflectiveExcelReader;
import com.reflectiveexcel.reader.detector.SimpleRowColumnDetector;
import org.junit.jupiter.api.Test;

public class ReadEventsTest {

    @Test
    public void readingTableWithPropertiesReturnsExpectedTableObject() {
        InputStream inputStream = getClass().getResourceAsStream("/test-table-and-properties.xlsx");

        ExcelReadingSettings settings = ExcelReadingSettings.builder()
                .rowColumnDetector(new SimpleRowColumnDetector("B5"))
                .build();

        ReflectiveExcelReader reader = new ReflectiveExcelReader(inputStream, settings);

        FoodExpensesTable table = reader.read(FoodExpensesTable.class);
        assertEquals(7, table.getMonth());
        assertEquals(2021, table.getYear());

        List<FoodExpenseRow> rows = table.getFoodExpenses();

        FoodExpensesTable.FoodExpenseRow firstRow = rows.get(0);
        assertEquals("Pasta", firstRow.getName());
        assertEquals(10, firstRow.getPrice());

        FoodExpensesTable.FoodExpenseRow secondRow = rows.get(1);
        assertEquals("Rice", secondRow.getName());
        assertEquals(20, secondRow.getPrice());
    }
}
