package com.mihai.multipletables.shipping;

import com.mihai.annotation.ExcelColumn;

public class ShippingCostRow {

    @ExcelColumn(name = "Supplier")
    private String supplier;

    @ExcelColumn(name = "Destination")
    private String destination;

    @ExcelColumn(name = "Units Shipped")
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
}
