package com.mihai;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;

public class CellValueFormatter {

    private final DataFormatter dataFormatter;
    private final FormulaEvaluator formulaEvaluator;

    public CellValueFormatter(Workbook workbook) {
        this.dataFormatter = new DataFormatter();
        dataFormatter.setUseCachedValuesForFormulaCells(true);
        this.formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
    }

    public String toString(Cell cell) {
        try {
            return dataFormatter.formatCellValue(cell, formulaEvaluator);
        }
        catch (IllegalStateException exception) {
            return dataFormatter.formatCellValue(cell, null);  // fallback
        }
    }
}
