package com.mihai.multipletables.supplier;

import com.mihai.annotation.ExcelColumn;

public class SupplierRow {

    @ExcelColumn(name = "Supplier")
    private String supplier;

    @ExcelColumn(name = "Capacity")
    private int capacity;

    public SupplierRow() {
    }

    public SupplierRow(String supplier, int capacity) {
        this.supplier = supplier;
        this.capacity = capacity;
    }

    public String getSupplier() {
        return supplier;
    }

    public int getCapacity() {
        return capacity;
    }
}
