package com.mihai.reader;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

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
            // fallback - can happen if a formula from a foreign workbook is referenced
            return dataFormatter.formatCellValue(cell, null);
        }
    }
}
