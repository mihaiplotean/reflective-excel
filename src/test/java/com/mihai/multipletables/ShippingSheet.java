package com.mihai.multipletables;

import com.mihai.annotation.ExcelRows;
import com.mihai.multipletables.destination.DestinationHeaderColumnDetector;
import com.mihai.multipletables.destination.DestinationHeaderRowDetector;
import com.mihai.multipletables.destination.DestinationRow;
import com.mihai.multipletables.shipping.ShippingCostHeaderColumnDetector;
import com.mihai.multipletables.shipping.ShippingCostHeaderRowDetector;
import com.mihai.multipletables.shipping.ShippingCostRow;
import com.mihai.multipletables.supplier.SupplierHeaderColumnDetector;
import com.mihai.multipletables.supplier.SupplierHeaderRowDetector;
import com.mihai.multipletables.supplier.SupplierRow;

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
}
