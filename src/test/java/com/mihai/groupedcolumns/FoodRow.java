package com.mihai.groupedcolumns;

import com.mihai.annotation.ExcelCellGroup;
import com.mihai.annotation.ExcelColumn;

public class FoodRow {

    @ExcelCellGroup(name = "pizza")
    private PizzaGroup group;

    @ExcelColumn(name = "id")
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
