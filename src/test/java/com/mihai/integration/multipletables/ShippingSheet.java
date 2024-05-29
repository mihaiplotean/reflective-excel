package com.mihai.integration.multipletables;

import com.mihai.core.annotation.TableId;

import java.util.List;

public class ShippingSheet {

    @TableId("Shipping Rows")
    private List<ShippingCostRow> shippingCostRows;

    @TableId("Supplier Rows")
    private List<SupplierRow> supplierRows;

    @TableId("Destination Rows")
    private List<DestinationRow> destinationRows;

    public ShippingSheet() {
    }

    public ShippingSheet(List<ShippingCostRow> shippingCostRows, List<SupplierRow> supplierRows, List<DestinationRow> destinationRows) {
        this.shippingCostRows = shippingCostRows;
        this.supplierRows = supplierRows;
        this.destinationRows = destinationRows;
    }

    public List<ShippingCostRow> getShippingCostRows() {
        return shippingCostRows;
    }

    public List<SupplierRow> getSupplierRows() {
        return supplierRows;
    }

    public List<DestinationRow> getDestinationRows() {
        return destinationRows;
    }
}
