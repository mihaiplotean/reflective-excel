package com.mihai.integration.multipletables;

import com.mihai.reader.ExcelReadingSettings;
import com.mihai.reader.ReflectiveExcelReader;
import com.mihai.writer.ReflectiveExcelWriter;
import com.mihai.writer.style.StyleProviders;
import com.mihai.writer.style.WritableCellStyles;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiTableTest {

    private static final List<ShippingCostRow> SHIPPING_COST_ROWS = List.of(
            new ShippingCostRow("Moldova", "USA", 1000),
            new ShippingCostRow("Macedonia", "Netherlands", 420)
    );
    private static final List<SupplierRow> SUPPLIER_ROWS = List.of(
            new SupplierRow("Moldova", 10000)
    );
    private static final List<DestinationRow> DESTINATION_ROWS = List.of(
            new DestinationRow("USA", 720, 100),
            new DestinationRow("Russia", 20, 15)
    );

    @Test
    public void testMultipleTablesInOneSheet() {
        InputStream inputStream = getClass().getResourceAsStream("/test-multi-table.xlsx");
        ExcelReadingSettings settings = ExcelReadingSettings.with()
                .autoRowColumnDetector()
                .create();
        ReflectiveExcelReader reader = new ReflectiveExcelReader(inputStream);
        ShippingSheet table = reader.read(ShippingSheet.class, settings);

        assertEquals(SHIPPING_COST_ROWS, table.getShippingCostRows());
        assertEquals(SUPPLIER_ROWS, table.getSupplierRows());
        assertEquals(DESTINATION_ROWS, table.getDestinationRows());
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
