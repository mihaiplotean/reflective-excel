package com.mihai.writer;

import com.mihai.annotation.ExcelColumn;

import java.time.LocalDate;
import java.util.Date;

public class FancyFoodExpensesTableRow {

    @ExcelColumn(name = "name")
    private String name;

    @ExcelColumn(name = "date")
    private LocalDate date;

    @ExcelColumn(name = "spent")
    private Money amount;

    public FancyFoodExpensesTableRow() {
    }

    public FancyFoodExpensesTableRow(String name, LocalDate date, Money amount) {
        this.name = name;
        this.date = date;
        this.amount = amount;
    }
}
