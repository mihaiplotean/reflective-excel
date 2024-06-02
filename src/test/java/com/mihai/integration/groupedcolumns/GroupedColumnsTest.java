package com.mihai.integration.groupedcolumns;

import com.mihai.assertion.ExcelAssert;
import com.mihai.core.annotation.ExcelCellGroup;
import com.mihai.core.annotation.ExcelColumn;
import com.mihai.reader.ReflectiveExcelReader;
import com.mihai.writer.ExcelWritingSettings;
import com.mihai.writer.ReflectiveExcelWriter;
import com.mihai.writer.style.StyleProviders;
import com.mihai.writer.style.WritableCellStyles;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupedColumnsTest {

    @Test
    public void readingTableWithGroupedColumnsReturnsExpectedRows() {
        InputStream inputStream = getClass().getResourceAsStream("/test-grouped-columns.xlsx");
        ReflectiveExcelReader reader = new ReflectiveExcelReader(inputStream);
        List<AddressesRow> rows = reader.readRows(AddressesRow.class);

        assertEquals(1, rows.size());

        AddressesRow row = rows.get(0);
        assertEquals(1, row.getId());
        assertEquals("street A", row.getAddressA().getHome());
        assertEquals("street B", row.getAddressA().getWork());
        assertEquals("street C", row.getAddressB().getHome());
        assertEquals("street D", row.getAddressB().getWork());
    }

    @Test
    public void writingTableWithGroupedColumnsGeneratesExpectedExcelFile() throws IOException {
        File actualFile = File.createTempFile("reflective-excel-writer", "grouped-columns-test.xlsx");

        List<AddressesRow> rows = List.of(
                new AddressesRow(1, new AddressesRow.Address("street A", "street B"), new AddressesRow.Address("street C", "street D"))
        );

        ExcelWritingSettings settings = ExcelWritingSettings.builder()
                .cellStyleProvider(StyleProviders.of(WritableCellStyles.allSideBorder()))
                .headerStyleProvider(StyleProviders.of(WritableCellStyles.boldText()))
                .build();
        new ReflectiveExcelWriter(actualFile, settings).writeRows(rows, AddressesRow.class);

        try (InputStream expectedInputStream = getClass().getResourceAsStream("/test-grouped-columns.xlsx")) {
            ExcelAssert.assertThat(actualFile)
                    .isEqualTo(expectedInputStream);
        } finally {
            actualFile.delete();
        }
    }

    @Test
    public void readingTableWithNestedGroupedColumnsReturnsExpectedRows() {
        InputStream inputStream = getClass().getResourceAsStream("/test-nested-grouped-columns.xlsx");

        ReflectiveExcelReader reader = new ReflectiveExcelReader(inputStream);
        List<FoodRow> rows = reader.readRows(FoodRow.class);

        assertEquals(1, rows.size());

        FoodRow row = rows.get(0);

        assertEquals(42, row.getId());
        FoodRow.PizzaGroup pizzaGroup = row.getGroup();
        assertEquals("20cm", pizzaGroup.getSize().getSmall());
        assertEquals("28cm", pizzaGroup.getSize().getLarge());
        assertEquals("Capriciosa", pizzaGroup.getPizzaName());
    }

    @Test
    public void writingTableWithNestedGroupedColumnsGeneratesExpectedExcelFile() throws IOException {
        File actualFile = File.createTempFile("reflective-excel-writer", "nested-grouped-columns-test.xlsx");

        List<FoodRow> rows = List.of(
                new FoodRow(new FoodRow.PizzaGroup(new FoodRow.PizzaGroup.PizzaSize("20cm", "28cm"), "Capriciosa"), 42)
        );

        ExcelWritingSettings settings = ExcelWritingSettings.builder()
                .headerStyleProvider(StyleProviders.of(WritableCellStyles.boldText().combineWith(WritableCellStyles.allSideBorder())))
                .build();
        ReflectiveExcelWriter writer = new ReflectiveExcelWriter(actualFile, settings);
        writer.writeRows(rows, FoodRow.class);

        try (InputStream expectedInputStream = getClass().getResourceAsStream("/test-nested-grouped-columns.xlsx")) {
            ExcelAssert.assertThat(actualFile)
                    .isEqualTo(expectedInputStream);
        } finally {
            actualFile.delete();
        }
    }

    public static class AddressesRow {

        @ExcelColumn("Id")
        private Integer id;

        @ExcelCellGroup(value = "Address A")
        private Address addressA;

        @ExcelCellGroup(value = "Address B")
        private Address addressB;

        public AddressesRow() {
        }

        public AddressesRow(Integer id, Address addressA, Address addressB) {
            this.id = id;
            this.addressA = addressA;
            this.addressB = addressB;
        }

        public Integer getId() {
            return id;
        }

        public Address getAddressA() {
            return addressA;
        }

        public Address getAddressB() {
            return addressB;
        }

        public static class Address {

            @ExcelColumn("home")
            private String home;

            @ExcelColumn("work")
            private String work;

            public Address() {
            }

            public Address(String home, String work) {
                this.home = home;
                this.work = work;
            }

            public String getHome() {
                return home;
            }

            public String getWork() {
                return work;
            }
        }
    }

    public static class FoodRow {

        @ExcelCellGroup(value = "pizza")
        private PizzaGroup group;

        @ExcelColumn("id")
        private Integer id;

        public FoodRow() {
        }

        public FoodRow(PizzaGroup group, Integer id) {
            this.group = group;
            this.id = id;
        }

        public PizzaGroup getGroup() {
            return group;
        }

        public Integer getId() {
            return id;
        }

        public static class PizzaGroup {

            @ExcelCellGroup(value = "size")
            private PizzaSize size;

            @ExcelColumn("name")
            private String pizzaName;

            public PizzaGroup() {
            }

            public PizzaGroup(PizzaSize size, String pizzaName) {
                this.size = size;
                this.pizzaName = pizzaName;
            }

            public PizzaSize getSize() {
                return size;
            }

            public String getPizzaName() {
                return pizzaName;
            }

            public static class PizzaSize {

                @ExcelColumn("small")
                private String small;

                @ExcelColumn("large")
                private String large;

                public PizzaSize() {
                }

                public PizzaSize(String small, String large) {
                    this.small = small;
                    this.large = large;
                }

                public String getSmall() {
                    return small;
                }

                public String getLarge() {
                    return large;
                }
            }
        }
    }
}
