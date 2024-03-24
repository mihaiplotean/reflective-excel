package com.mihai.integration.row;

import com.mihai.annotation.ExcelColumn;

public class FoodExpenseRow {

    @ExcelColumn(name = "Food")
    private String name;

    @ExcelColumn(name = "Price")
    private double price;

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
