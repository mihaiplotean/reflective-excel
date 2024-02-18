package com.mihai.groupedcolumns;

import com.mihai.ReflectiveExcelReader;
import com.mihai.writer.ReflectiveExcelWriter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupedColumnsTest {

    @Test
    public void testReadGroupedCells() {
        InputStream inputStream = getClass().getResourceAsStream("/test-address-group.xlsx");

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
    public void testWriteGroupedCells() throws IOException {
        File tempFile = File.createTempFile("reflective-excel-writer", "grouped-columns-test.xlsx");

        List<AddressesRow> rows = List.of(
                new AddressesRow(1, new Address("street A", "street B"), new Address("street C", "street D"))
        );

        new ReflectiveExcelWriter(tempFile).writeRows(rows, AddressesRow.class);
    }

    @Test
    public void testReadNestedGroupedCells() {
        InputStream inputStream = getClass().getResourceAsStream("/test-address-group-nested.xlsx");

        ReflectiveExcelReader reader = new ReflectiveExcelReader(inputStream);
        List<FoodRow> rows = reader.readRows(FoodRow.class);

        assertEquals(1, rows.size());

        FoodRow row = rows.get(0);

        assertEquals(42, row.getId());
        PizzaGroup pizzaGroup = row.getGroup();
        assertEquals("20cm", pizzaGroup.getSize().getSmall());
        assertEquals("28cm", pizzaGroup.getSize().getLarge());
        assertEquals("Capriciosa", pizzaGroup.getPizzaName());
    }

    @Test
    public void testWriteNestedGroupedCells() throws IOException {
        File tempFile = File.createTempFile("reflective-excel-writer", "nested-grouped-columns-test.xlsx");

        List<FoodRow> rows = List.of(
                new FoodRow(new PizzaGroup(new PizzaSize("20cm", "28cm"), "Capriciosa"), 42)
        );

        new ReflectiveExcelWriter(tempFile).writeRows(rows, FoodRow.class);
    }
}
