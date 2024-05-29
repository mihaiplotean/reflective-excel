package com.mihai.integration.row;

import com.mihai.core.annotation.ExcelColumn;

public class FoodExpenseRow {

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
