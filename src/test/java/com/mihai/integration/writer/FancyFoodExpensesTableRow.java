package com.mihai.integration.writer;

import com.mihai.common.annotation.ExcelColumn;

import java.time.LocalDate;

public class FancyFoodExpensesTableRow {

    @ExcelColumn("name")
    private String name;

    @ExcelColumn("date")
    private LocalDate date;

    @ExcelColumn("spent")
    private Money amount;

    public FancyFoodExpensesTableRow() {
    }

    public FancyFoodExpensesTableRow(String name, LocalDate date, Money amount) {
        this.name = name;
        this.date = date;
        this.amount = amount;
    }
}
