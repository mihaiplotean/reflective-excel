package com.mihai.row;

import com.mihai.DynamicColumns;
import com.mihai.ExcelColumn;
import com.mihai.ExcelRow;

import java.util.Map;

@ExcelRow
public class PopulationRow {

    @ExcelColumn(name = "Id")
    private Integer id;

    @ExcelColumn(name = "Country")
    private String country;

    @DynamicColumns(detector = PopulationRowDynamicColumnDetector.class)
    private Map<Integer, Integer> populationPerYear;

    public PopulationRow() {
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
