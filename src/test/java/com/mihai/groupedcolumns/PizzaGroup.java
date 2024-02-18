package com.mihai.groupedcolumns;

import com.mihai.annotation.ExcelCellGroup;
import com.mihai.annotation.ExcelColumn;

public class PizzaGroup {

    @ExcelCellGroup(name = "size")
    private PizzaSize size;

    @ExcelColumn(name = "name")
    private String pizzaName;

    public PizzaGroup(PizzaSize size, String pizzaName) {
        this.size = size;
        this.pizzaName = pizzaName;
    }

    public PizzaSize getSize() {
        return size;
    }

    public String getPizzaName() {
        return pizzaName;
    }
}
