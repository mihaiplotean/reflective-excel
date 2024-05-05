package com.mihai.integration.dynamiccolumns;

import com.mihai.common.annotation.DynamicColumns;
import com.mihai.common.annotation.ExcelColumn;

import java.util.Map;

public class PopulationRow {

    @ExcelColumn(name = "Id")
    private Integer id;

    @ExcelColumn(name = "Country")
    private String country;

    @DynamicColumns(detector = PopulationRowDynamicColumnDetector.class)
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
}
