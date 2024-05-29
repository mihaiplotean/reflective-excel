package com.mihai.integration.groupedcolumns;

import com.mihai.core.annotation.ExcelCellGroup;
import com.mihai.core.annotation.ExcelColumn;

public class PizzaGroup {

    @ExcelCellGroup(value = "size")
    private PizzaSize size;

    @ExcelColumn("name")
    private String pizzaName;

    public PizzaGroup() {
    }

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
