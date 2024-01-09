package com.mihai.groupedcolumns;

import com.mihai.annotation.ExcelColumn;

public class Address {

    @ExcelColumn(name = "home")
    private String home;

    @ExcelColumn(name = "work")
    private String work;

    public String getHome() {
        return home;
    }

    public String getWork() {
        return work;
    }
}
