package com.mihai.integration.multipletables;

import com.mihai.annotation.ExcelRows;
import com.mihai.integration.multipletables.destination.DestinationRow;
import com.mihai.integration.multipletables.shipping.ShippingCostHeaderRowDetector;
import com.mihai.integration.multipletables.shipping.ShippingCostRow;
import com.mihai.integration.multipletables.supplier.SupplierHeaderColumnDetector;
import com.mihai.integration.multipletables.supplier.SupplierHeaderRowDetector;
import com.mihai.integration.multipletables.supplier.SupplierRow;
import com.mihai.integration.multipletables.destination.DestinationHeaderColumnDetector;
import com.mihai.integration.multipletables.destination.DestinationHeaderRowDetector;
import com.mihai.integration.multipletables.shipping.ShippingCostHeaderColumnDetector;

import java.util.List;

public class ShippingSheet {

    @ExcelRows(headerRowDetector = ShippingCostHeaderRowDetector.class,
            headerStartColumnDetector = ShippingCostHeaderColumnDetector.class)
    private List<ShippingCostRow> shippingCostRows;

    @ExcelRows(headerRowDetector = SupplierHeaderRowDetector.class,
            headerStartColumnDetector = SupplierHeaderColumnDetector.class)
    private List<SupplierRow> supplierRows;

    @ExcelRows(headerRowDetector = DestinationHeaderRowDetector.class,
            headerStartColumnDetector = DestinationHeaderColumnDetector.class)
    private List<DestinationRow> destinationRows;

    public ShippingSheet() {
    }

    public ShippingSheet(List<ShippingCostRow> shippingCostRows, List<SupplierRow> supplierRows, List<DestinationRow> destinationRows) {
        this.shippingCostRows = shippingCostRows;
        this.supplierRows = supplierRows;
        this.destinationRows = destinationRows;
    }
}
