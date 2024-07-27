package com.reflectiveexcel.integration.dynamiccolumns;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.reflectiveexcel.assertion.ExcelAssert;
import com.reflectiveexcel.core.annotation.DynamicColumns;
import com.reflectiveexcel.core.annotation.ExcelColumn;
import com.reflectiveexcel.reader.ReadingContext;
import com.reflectiveexcel.reader.ReflectiveExcelReader;
import com.reflectiveexcel.reader.detector.DynamicColumnDetector;
import com.reflectiveexcel.reader.detector.MaybeDynamicColumn;
import com.reflectiveexcel.writer.ExcelWritingSettings;
import com.reflectiveexcel.writer.ReflectiveExcelWriter;
import com.reflectiveexcel.writer.style.CellStyleContext;
import com.reflectiveexcel.writer.style.DefaultStyleContext;
import com.reflectiveexcel.writer.style.StyleProviders;
import com.reflectiveexcel.writer.style.WritableCellStyles;
import org.junit.jupiter.api.Test;

public class DynamicColumnsTest {

    @Test
    public void readingTableWithDynamicColumnsReturnsExpectedRows() {
        InputStream inputStream = getClass().getResourceAsStream("/test-dynamic-columns.xlsx");

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
    public void writingDynamicColumnsGeneratesExpectedExcelFile() throws IOException {
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
        File actualFile = File.createTempFile("reflective-excel-writer", "dynamic-columns-test.xlsx");

        CellStyleContext styleContext = new DefaultStyleContext();
        styleContext.setHeaderStyleProvider(StyleProviders.of(WritableCellStyles.boldText()));

        ExcelWritingSettings settings = ExcelWritingSettings.builder()
                .cellStyleContext(styleContext)
                .build();
        ReflectiveExcelWriter writer = new ReflectiveExcelWriter(actualFile, settings);
        writer.writeRows(rows, PopulationRow.class);

        try (InputStream expectedInputStream = getClass().getResourceAsStream("/test-dynamic-columns.xlsx")) {
            ExcelAssert.assertThat(actualFile)
                    .isEqualTo(expectedInputStream);
        } finally {
            actualFile.delete();
        }
    }

    public static class PopulationRow {

        @ExcelColumn("Id")
        private Integer id;

        @ExcelColumn("Country")
        private String country;

        @DynamicColumns(detector = PopulationRowDynamicDynamicColumnDetector.class)
        private Map<Integer, Integer> populationPerYear;

        public PopulationRow() {
        }

        public PopulationRow(Integer id, String country, Map<Integer, Integer> populationPerYear) {
            this.id = id;
            this.country = country;
            this.populationPerYear = populationPerYear;
        }

        public Integer getId() {
            return id;
        }

        public String getCountry() {
            return country;
        }

        public int getPopulation(int year) {
            return populationPerYear.getOrDefault(year, -1);
        }

        public static class PopulationRowDynamicDynamicColumnDetector implements DynamicColumnDetector {

            @Override
            public boolean test(ReadingContext context, MaybeDynamicColumn column) {
                return column.isAfter("country");
            }
        }
    }
}
