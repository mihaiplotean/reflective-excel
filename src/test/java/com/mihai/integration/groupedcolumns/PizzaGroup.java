package com.mihai.integration.groupedcolumns;

import com.mihai.common.annotation.ExcelCellGroup;
import com.mihai.common.annotation.ExcelColumn;

public class PizzaGroup {

    @ExcelCellGroup(name = "size")
    private PizzaSize size;

    @ExcelColumn(name = "name")
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
