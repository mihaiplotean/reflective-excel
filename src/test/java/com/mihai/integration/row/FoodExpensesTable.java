package com.mihai.integration.row;

import com.mihai.annotation.ExcelProperty;

import java.util.List;

public class FoodExpensesTable {

    @ExcelProperty(name = "Month", nameReference = "B2", valueReference = "C2")
    private int month;

    @ExcelProperty(name = "Year", nameReference = "B3", valueReference = "C3")
    private int year;

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
}
