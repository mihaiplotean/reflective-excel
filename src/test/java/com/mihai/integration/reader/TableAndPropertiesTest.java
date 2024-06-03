package com.mihai.integration.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.util.List;

import com.mihai.core.annotation.ExcelColumn;
import com.mihai.core.annotation.ExcelProperty;
import com.mihai.core.annotation.TableId;
import com.mihai.reader.ExcelReadingSettings;
import com.mihai.reader.ReflectiveExcelReader;
import org.junit.jupiter.api.Test;

public class TableAndPropertiesTest {

    @Test
    public void readingTableWithPropertiesReturnsExpectedTableObject() {
        InputStream inputStream = getClass().getResourceAsStream("/test-table-and-properties.xlsx");

        ExcelReadingSettings settings = ExcelReadingSettings.builder()
                .headerStartCellReference("B5")
                .build();

        ReflectiveExcelReader reader = new ReflectiveExcelReader(inputStream, settings);

        FoodExpensesTable table = reader.read(FoodExpensesTable.class);
        assertEquals(7, table.getMonth());
        assertEquals(2021, table.getYear());

        List<FoodExpensesTable.FoodExpenseRow> rows = table.getFoodExpenses();

        FoodExpensesTable.FoodExpenseRow firstRow = rows.get(0);
        assertEquals("Pasta", firstRow.getName());
        assertEquals(10, firstRow.getPrice());

        FoodExpensesTable.FoodExpenseRow secondRow = rows.get(1);
        assertEquals("Rice", secondRow.getName());
        assertEquals(20, secondRow.getPrice());
    }

    public static class FoodExpensesTable {

        @ExcelProperty(name = "Month", nameReference = "B2", valueReference = "C2")
        private int month;

        @ExcelProperty(name = "Year", nameReference = "B3", valueReference = "C3")
        private int year;

        @TableId("food-expenses")
        private List<FoodExpenseRow> foodExpenses;

        public int getMonth() {
            return month;
        }

        public int getYear() {
            return year;
        }

        public List<FoodExpenseRow> getFoodExpenses() {
            return foodExpenses;
        }

        public static class FoodExpenseRow {

            @ExcelColumn("Food")
            private String name;

            @ExcelColumn("Price")
            private double price;

            public String getName() {
                return name;
            }

            public double getPrice() {
                return price;
            }
        }
    }
}
