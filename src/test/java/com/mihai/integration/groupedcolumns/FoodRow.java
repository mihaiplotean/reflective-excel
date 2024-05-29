package com.mihai.integration.groupedcolumns;

import com.mihai.core.annotation.ExcelCellGroup;
import com.mihai.core.annotation.ExcelColumn;

public class FoodRow {

    @ExcelCellGroup(value = "pizza")
    private PizzaGroup group;

    @ExcelColumn("id")
    private Integer id;

    public FoodRow() {
    }

    public FoodRow(PizzaGroup group, Integer id) {
        this.group = group;
        this.id = id;
    }

    public PizzaGroup getGroup() {
        return group;
    }

    public Integer getId() {
        return id;
    }
}
