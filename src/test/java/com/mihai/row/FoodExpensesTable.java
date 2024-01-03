package com.mihai.row;

import com.mihai.annotation.ExcelProperty;
import com.mihai.annotation.ExcelRows;

import java.util.List;

public class FoodExpensesTable {

    @ExcelProperty(name = "Month", cellReference = "B2")
    private int month;

    @ExcelProperty(name = "Year", cellReference = "B3")
    private int year;

    @ExcelRows
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
