package com.mihai.row;

import com.mihai.annotation.ExcelProperty;

public class FoodExpensesProperties {

    @ExcelProperty(name = "Month", cellReference = "B2")
    private int month;
    @ExcelProperty(name = "Year", cellReference = "B3")
    private int year;

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
