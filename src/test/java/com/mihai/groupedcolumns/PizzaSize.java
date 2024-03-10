package com.mihai.groupedcolumns;

import com.mihai.annotation.ExcelColumn;

public class PizzaSize {

    @ExcelColumn(name = "small")
    private String small;

    @ExcelColumn(name = "large")
    private String large;

    public PizzaSize() {
    }

    public PizzaSize(String small, String large) {
        this.small = small;
        this.large = large;
    }

    public String getSmall() {
        return small;
    }

    public String getLarge() {
        return large;
    }
}
