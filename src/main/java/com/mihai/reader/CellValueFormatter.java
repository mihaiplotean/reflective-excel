package com.mihai.reader;

import com.mihai.core.utils.DateFormatUtils;
import org.apache.poi.ss.usermodel.*;

public class CellValueFormatter {

    private final DataFormatter dataFormatter;
    private final FormulaEvaluator formulaEvaluator;

    public CellValueFormatter(Workbook workbook) {
        this.dataFormatter = new DataFormatter();
        dataFormatter.setUseCachedValuesForFormulaCells(true);
        localizeDateFormat();

        this.formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
    }

    /**
     * Excel only stores the index of the default date format. That index is 14. When the Excel file is opened,
     * the default date format is localized to the system locale. Apache POI does not account for this, so we do
     * that here.
     *
     * @see <a href="https://stackoverflow.com/a/34902174">Excel Cell Style issue</a>
     */
    private void localizeDateFormat() {
        dataFormatter.addFormat(BuiltinFormats.getBuiltinFormat(DateFormatUtils.DEFAULT_DATE_FORMAT_INDEX),
                DateFormatUtils.getLocalizedDateFormat());
    }

    public String toString(Cell cell) {
        try {
            return dataFormatter.formatCellValue(cell, formulaEvaluator);
        } catch (IllegalStateException exception) {
            // fallback - can happen if a formula from a foreign workbook is referenced
            return dataFormatter.formatCellValue(cell, null);
        }
    }
}
