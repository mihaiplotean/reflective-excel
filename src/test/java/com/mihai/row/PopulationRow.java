package com.mihai.row;

import com.mihai.annotation.DynamicColumns;
import com.mihai.annotation.ExcelColumn;
import com.mihai.annotation.ExcelRow;

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
