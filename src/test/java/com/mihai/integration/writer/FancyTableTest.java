package com.mihai.integration.writer;

import com.mihai.writer.ExcelWritingSettings;
import com.mihai.writer.ReflectiveExcelWriter;
import com.mihai.writer.WritingContext;
import com.mihai.common.workbook.CellLocation;
import com.mihai.writer.style.StyleProvider;
import com.mihai.writer.style.StyleProviders;
import com.mihai.writer.style.WritableCellStyle;
import com.mihai.writer.style.WritableCellStyles;
import com.mihai.writer.style.color.StyleColor;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class FancyTableTest {

    @Test
    public void testFancyTable() throws IOException {
        Currency eurCurrency = Currency.getInstance("EUR");
        Currency usdCurrency = Currency.getInstance("USD");
        List<FancyFoodExpensesTableRow> rows = List.of(
                new FancyFoodExpensesTableRow("pizza", LocalDate.of(2022, 11, 1), new Money(120, eurCurrency)),
                new FancyFoodExpensesTableRow("carbonara", LocalDate.of(2022, 11, 2), new Money(90, usdCurrency)),
                new FancyFoodExpensesTableRow("placinte", LocalDate.of(2022, 11, 3), new Money(90, usdCurrency))
        );

        File actualFile = File.createTempFile("reflective-excel-writer", "fancy-table-test.xlsx");

        ExcelWritingSettings settings = ExcelWritingSettings.builder()
                .tableStartCellLocator((context, tableId) -> CellLocation.fromReference("C3"))
                .headerStyleProvider(StyleProviders.of(WritableCellStyles.boldText()))
                .cellStyleProvider(StyleProviders.of(WritableCellStyles.allSideBorder()))
                .rowStyleProvider(StyleProviders.stripedRows(new StyleColor(240, 248, 255), null))
                .registerSerializer(Money.class, Money::getAmount)
                .registerTypeStyleProvider(Money.class, new StyleProvider() {

                    private static final String CURRENCY_FORMAT = "_(%s* #,##_);_(%<s * -#,##_);_(%<s* \"\"-\"\"??_);_(@_)";

                    @Override
                    public WritableCellStyle getStyle(WritingContext context, Object target) {
                        Money money = (Money) target;
                        return WritableCellStyle.builder()
                                .format(CURRENCY_FORMAT.formatted(money.getCurrencySymbol(Locale.US)))
                                .build();
                    }
                })
                .build();

        ReflectiveExcelWriter writer = new ReflectiveExcelWriter(actualFile, settings);

        writer.writeRows(rows, FancyFoodExpensesTableRow.class);
    }
}
