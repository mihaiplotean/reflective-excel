package com.mihai.integration.multipletables;

import com.mihai.annotation.TableId;
import com.mihai.integration.multipletables.destination.DestinationRow;
import com.mihai.integration.multipletables.shipping.ShippingCostRow;
import com.mihai.integration.multipletables.supplier.SupplierRow;

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
}
