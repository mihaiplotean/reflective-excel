package com.mihai.integration.multipletables;

import com.mihai.core.annotation.ExcelColumn;

import java.util.Objects;

public class ShippingCostRow {

    @ExcelColumn("Supplier")
    private String supplier;

    @ExcelColumn("Destination")
    private String destination;

    @ExcelColumn("Units Shipped")
    private int unitsShipped;

    public ShippingCostRow() {
    }

    public ShippingCostRow(String supplier, String destination, int unitsShipped) {
        this.supplier = supplier;
        this.destination = destination;
        this.unitsShipped = unitsShipped;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getDestination() {
        return destination;
    }

    public int getUnitsShipped() {
        return unitsShipped;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShippingCostRow that = (ShippingCostRow) o;
        return unitsShipped == that.unitsShipped
                && Objects.equals(supplier, that.supplier)
                && Objects.equals(destination, that.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(supplier, destination, unitsShipped);
    }
}
