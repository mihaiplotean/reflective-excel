package com.mihai.integration.groupedcolumns;

import com.mihai.core.annotation.ExcelColumn;

public class PizzaSize {

    @ExcelColumn("small")
    private String small;

    @ExcelColumn("large")
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
