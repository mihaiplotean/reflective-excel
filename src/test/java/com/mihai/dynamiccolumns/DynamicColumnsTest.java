package com.mihai.dynamiccolumns;

import com.mihai.ReflectiveExcelReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamicColumnsTest {

    @Test
    public void testDynamicColumns() {
        InputStream inputStream = getClass().getResourceAsStream("/test-population.xlsx");

        List<PopulationRow> rows = new ReflectiveExcelReader(inputStream).readRows(PopulationRow.class);

        PopulationRow firstRow = rows.get(0);
        assertEquals(1, firstRow.getId());
        assertEquals("Moldova", firstRow.getCountry());
        assertEquals(120, firstRow.getPopulation(2021));
        assertEquals(130, firstRow.getPopulation(2022));
        assertEquals(200, firstRow.getPopulation(2023));

        PopulationRow secondRow = rows.get(1);
        assertEquals(2, secondRow.getId());
        assertEquals("Netherlands", secondRow.getCountry());
        assertEquals(321, secondRow.getPopulation(2021));
        assertEquals(420, secondRow.getPopulation(2022));
        assertEquals(500, secondRow.getPopulation(2023));
    }
}
