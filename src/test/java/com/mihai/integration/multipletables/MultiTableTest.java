package com.mihai.integration.multipletables;

import com.mihai.assertion.ExcelAssert;
import com.mihai.core.annotation.ExcelColumn;
import com.mihai.core.annotation.TableId;
import com.mihai.core.workbook.CellLocation;
import com.mihai.reader.ExcelReadingSettings;
import com.mihai.reader.ReflectiveExcelReader;
import com.mihai.writer.ExcelWritingSettings;
import com.mihai.writer.ReflectiveExcelWriter;
import com.mihai.writer.WritingContext;
import com.mihai.writer.locator.TableStartCellLocator;
import com.mihai.writer.style.StyleProviders;
import com.mihai.writer.style.WritableCellStyles;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiTableTest {

    private static final List<ShippingSheet.ShippingCostRow> SHIPPING_COST_ROWS = List.of(
            new ShippingSheet.ShippingCostRow("Moldova", "USA", 1000),
            new ShippingSheet.ShippingCostRow("Macedonia", "Netherlands", 420)
    );
    private static final List<ShippingSheet.SupplierRow> SUPPLIER_ROWS = List.of(
            new ShippingSheet.SupplierRow("Moldova", 10000)
    );
    private static final List<ShippingSheet.DestinationRow> DESTINATION_ROWS = List.of(
            new ShippingSheet.DestinationRow("USA", 720, 100),
            new ShippingSheet.DestinationRow("Russia", 20, 15)
    );

    @Test
    public void readingMultipleTablesReturnsExpectedTableObjects() {
        InputStream inputStream = getClass().getResourceAsStream("/test-multiple-tables.xlsx");
        ExcelReadingSettings settings = ExcelReadingSettings.builder()
                .autoRowColumnDetector()
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

        File actualFile = File.createTempFile("reflective-excel-writer", "multi-table.xlsx");
        ReflectiveExcelWriter writer = new ReflectiveExcelWriter(actualFile, ExcelWritingSettings.builder()
                .headerStyleProvider(StyleProviders.of(WritableCellStyles.boldText()))
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
        }
        finally {
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
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
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
