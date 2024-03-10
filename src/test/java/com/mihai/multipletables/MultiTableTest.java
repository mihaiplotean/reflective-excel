package com.mihai.multipletables;

import com.mihai.groupedcolumns.FoodRow;
import com.mihai.groupedcolumns.PizzaGroup;
import com.mihai.groupedcolumns.PizzaSize;
import com.mihai.multipletables.destination.DestinationRow;
import com.mihai.multipletables.shipping.ShippingCostRow;
import com.mihai.multipletables.supplier.SupplierRow;
import com.mihai.reader.ReflectiveExcelReader;
import com.mihai.writer.ReflectiveExcelWriter;
import com.mihai.writer.style.StyleProviders;
import com.mihai.writer.style.WritableCellStyles;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MultiTableTest {

    @Test
    public void testMultipleTablesInOneSheet() {
        InputStream inputStream = getClass().getResourceAsStream("/test-multi-table.xlsx");
        ReflectiveExcelReader reader = new ReflectiveExcelReader(inputStream);
        ShippingSheet table = reader.read(ShippingSheet.class);

        System.out.println("test");
    }

    @Test
    public void testWriteMultipleTablesInOneSheet() throws IOException {
        File tempFile = File.createTempFile("reflective-excel-writer", "multi-table.xlsx");

        List<ShippingCostRow> shippingCostRows = List.of(
                new ShippingCostRow("China", "Chille", 20),
                new ShippingCostRow("Moldova", "Congo", 100));

        List<SupplierRow> supplierRows = List.of(
                new SupplierRow("Ecuador", 12)
        );

        List<DestinationRow> destinationRows = List.of(
                new DestinationRow("India", 10000, 900),
                new DestinationRow("Uzbekistan", 200, 120),
                new DestinationRow("Germany", 20, 19)
        );

        ShippingSheet sheet = new ShippingSheet(shippingCostRows, supplierRows, destinationRows);

        ReflectiveExcelWriter writer = new ReflectiveExcelWriter(tempFile);
        writer.setHeaderStyleProvider(StyleProviders.of(WritableCellStyles.boldText()));
        writer.write(sheet);

        System.out.println("test done");
    }
}
