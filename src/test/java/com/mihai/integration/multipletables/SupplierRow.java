package com.mihai.integration.multipletables;

import com.mihai.common.annotation.ExcelColumn;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupplierRow that = (SupplierRow) o;
        return capacity == that.capacity
                && Objects.equals(supplier, that.supplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(supplier, capacity);
    }
}
