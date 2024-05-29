package com.mihai.integration.dynamiccolumns;

import com.mihai.reader.ReflectiveExcelReader;
import com.mihai.writer.ReflectiveExcelWriter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamicColumnsTest {

    // todo: make integration tests use concepts instead of imaginary scenarios
    @Test
    public void testReadDynamicColumns() {
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

    @Test
    public void testWriteDynamicColumns() throws IOException {
        Map<Integer, Integer> mdPopulationPerYear = new LinkedHashMap<>();
        mdPopulationPerYear.put(2021, 120);
        mdPopulationPerYear.put(2022, 130);
        mdPopulationPerYear.put(2023, 200);

        Map<Integer, Integer> nlPopulationPerYear = new LinkedHashMap<>();
        nlPopulationPerYear.put(2021, 321);
        nlPopulationPerYear.put(2022, 420);
        nlPopulationPerYear.put(2023, 500);

        List<PopulationRow> rows = List.of(
                new PopulationRow(1, "Moldova", mdPopulationPerYear),
                new PopulationRow(2, "Netherlands", nlPopulationPerYear)
        );
        File tempFile = File.createTempFile("reflective-excel-writer", "dynamic-columns-test.xlsx");
        ReflectiveExcelWriter writer = new ReflectiveExcelWriter(tempFile);
        writer.writeRows(rows, PopulationRow.class);
    }
}
