package com.mihai.integration.groupedcolumns;

import com.mihai.common.annotation.ExcelColumn;

public class Address {

    @ExcelColumn("home")
    private String home;

    @ExcelColumn("work")
    private String work;

    public Address() {
    }

    public Address(String home, String work) {
        this.home = home;
        this.work = work;
    }

    public String getHome() {
        return home;
    }

    public String getWork() {
        return work;
    }
}
