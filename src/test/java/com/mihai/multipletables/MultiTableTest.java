package com.mihai.multipletables;

import com.mihai.ExcelReadingSettings;
import com.mihai.ReflectiveExcelReader;
import com.mihai.row.FoodExpensesTable;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class MultiTableTest {

    @Test
    public void testMultipleTablesInOneSheet() {
        InputStream inputStream = getClass().getResourceAsStream("/test-multi-table.xlsx");
        ReflectiveExcelReader reader = new ReflectiveExcelReader(inputStream);
        ShippingSheet table = reader.read(ShippingSheet.class);

        System.out.println("test");
    }
}
