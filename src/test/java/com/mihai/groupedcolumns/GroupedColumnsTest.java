package com.mihai.groupedcolumns;

import com.mihai.ReflectiveExcelReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupedColumnsTest {

    @Test
    public void testGroupedCells() {
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
    public void testNestedGroupedCells() {
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
}
