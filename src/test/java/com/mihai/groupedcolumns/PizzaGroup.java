package com.mihai.groupedcolumns;

import com.mihai.annotation.ExcelCellGroup;
import com.mihai.annotation.ExcelColumn;

public class PizzaGroup {

    @ExcelCellGroup(name = "size")
    private PizzaSize size;

    @ExcelColumn(name = "name")
    private String pizzaName;

    public PizzaSize getSize() {
        return size;
    }

    public String getPizzaName() {
        return pizzaName;
    }
}
