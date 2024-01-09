package com.mihai.groupedcolumns;

import com.mihai.annotation.ExcelColumn;

public class PizzaSize {

    @ExcelColumn(name = "small")
    private String small;

    @ExcelColumn(name = "large")
    private String large;

    public String getSmall() {
        return small;
    }

    public String getLarge() {
        return large;
    }
}
