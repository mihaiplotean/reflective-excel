package com.reflectiveexcel.integration.multipletables;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import com.reflectiveexcel.assertion.ExcelAssert;
import com.reflectiveexcel.core.annotation.ExcelColumn;
import com.reflectiveexcel.core.annotation.TableId;
import com.reflectiveexcel.core.workbook.CellLocation;
import com.reflectiveexcel.integration.multipletables.MultiTableTest.ShippingSheet.DestinationRow;
import com.reflectiveexcel.integration.multipletables.MultiTableTest.ShippingSheet.ShippingCostRow;
import com.reflectiveexcel.integration.multipletables.MultiTableTest.ShippingSheet.SupplierRow;
import com.reflectiveexcel.reader.ExcelReadingSettings;
import com.reflectiveexcel.reader.ReflectiveExcelReader;
import com.reflectiveexcel.reader.detector.AutoRowColumnDetector;
import com.reflectiveexcel.writer.ExcelWritingSettings;
import com.reflectiveexcel.writer.ReflectiveExcelWriter;
import com.reflectiveexcel.writer.style.CellStyleContext;
import com.reflectiveexcel.writer.style.DefaultStyleContext;
import com.reflectiveexcel.writer.style.StyleProviders;
import com.reflectiveexcel.writer.style.WritableCellStyles;
import org.junit.jupiter.api.Test;

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
    public void readingMultipleTablesReturnsExpectedTableObjects() {
        InputStream inputStream = getClass().getResourceAsStream("/test-multiple-tables.xlsx");
        ExcelReadingSettings settings = ExcelReadingSettings.builder()
                .rowColumnDetector(new AutoRowColumnDetector())
                .build();
        ReflectiveExcelReader reader = new ReflectiveExcelReader(inputStream, settings);
        ShippingSheet table = reader.read(ShippingSheet.class);

        assertEquals(SHIPPING_COST_ROWS, table.getShippingCostRows());
        assertEquals(SUPPLIER_ROWS, table.getSupplierRows());
        assertEquals(DESTINATION_ROWS, table.getDestinationRows());
    }

    @Test
    public void writingMultipleTablesGeneratesExpectedExcelFile() throws IOException {
        ShippingSheet sheet = new ShippingSheet(SHIPPING_COST_ROWS, SUPPLIER_ROWS, DESTINATION_ROWS);

        CellStyleContext styleContext = new DefaultStyleContext();
        styleContext.setHeaderStyleProvider(StyleProviders.of(WritableCellStyles.boldText()));

        File actualFile = File.createTempFile("reflective-excel-writer", "multi-table.xlsx");
        ReflectiveExcelWriter writer = new ReflectiveExcelWriter(actualFile, ExcelWritingSettings.builder()
                .cellStyleContext(styleContext)
                .tableStartCellLocator((context, tableId) -> {
                    if (tableId.equalsIgnoreCase("Shipping Rows")) {
                        return CellLocation.fromReference("B4");
                    }
                    if (tableId.equalsIgnoreCase("Supplier Rows")) {
                        return CellLocation.fromReference("G4");
                    }
                    if (tableId.equalsIgnoreCase("Destination Rows")) {
                        return CellLocation.fromReference("D12");
                    }
                    return new CellLocation(0, 0);
                })
                .build());
        writer.write(sheet);

        try (InputStream expectedInputStream = getClass().getResourceAsStream("/test-multiple-tables.xlsx")) {
            ExcelAssert.assertThat(actualFile)
                    .isEqualTo(expectedInputStream);
        } finally {
            actualFile.delete();
        }
    }

    public static class ShippingSheet {

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

        public static class ShippingCostRow {

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
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
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

        public static class SupplierRow {

            @ExcelColumn("Supplier")
            private String supplier;

            @ExcelColumn("Capacity")
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
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                SupplierRow that = (SupplierRow) o;
                return capacity == that.capacity
                        && Objects.equals(supplier, that.supplier);
            }

            @Override
            public int hashCode() {
                return Objects.hash(supplier, capacity);
            }
        }

        public static class DestinationRow {

            @ExcelColumn("Destination")
            private String destination;

            @ExcelColumn("required")
            private int requiredNum;

            @ExcelColumn("delivered")
            private int deliveredNum;

            public DestinationRow() {
            }

            public DestinationRow(String destination, int requiredNum, int deliveredNum) {
                this.destination = destination;
                this.requiredNum = requiredNum;
                this.deliveredNum = deliveredNum;
            }

            public String getDestination() {
                return destination;
            }

            public int getRequiredNum() {
                return requiredNum;
            }

            public int getDeliveredNum() {
                return deliveredNum;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                DestinationRow that = (DestinationRow) o;
                return requiredNum == that.requiredNum
                        && deliveredNum == that.deliveredNum
                        && Objects.equals(destination, that.destination);
            }

            @Override
            public int hashCode() {
                return Objects.hash(destination, requiredNum, deliveredNum);
            }
        }
    }
}
