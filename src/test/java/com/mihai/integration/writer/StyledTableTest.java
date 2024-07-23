package com.mihai.integration.writer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import com.mihai.assertion.ExcelAssert;
import com.mihai.core.annotation.ExcelColumn;
import com.mihai.core.workbook.CellLocation;
import com.mihai.integration.writer.StyledTableTest.FoodExpensesRow.Money;
import com.mihai.writer.ExcelWritingSettings;
import com.mihai.writer.ReflectiveExcelWriter;
import com.mihai.writer.WritingContext;
import com.mihai.writer.serializer.DefaultSerializationContext;
import com.mihai.writer.serializer.SerializationContext;
import com.mihai.writer.style.CellStyleContext;
import com.mihai.writer.style.DefaultStyleContext;
import com.mihai.writer.style.StyleProvider;
import com.mihai.writer.style.StyleProviders;
import com.mihai.writer.style.WritableCellStyle;
import com.mihai.writer.style.WritableCellStyles;
import com.mihai.writer.style.color.StyleColor;
import org.junit.jupiter.api.Test;

public class StyledTableTest {

    @Test
    public void writingTableWithStylesGeneratesExpectedExcelFile() throws IOException {
        Currency eurCurrency = Currency.getInstance("EUR");
        Currency usdCurrency = Currency.getInstance("USD");
        List<FoodExpensesRow> rows = List.of(
                new FoodExpensesRow("pizza", LocalDate.of(2022, 11, 1), new Money(120, eurCurrency)),
                new FoodExpensesRow("carbonara", LocalDate.of(2022, 11, 2), new Money(90, usdCurrency)),
                new FoodExpensesRow("placinte", LocalDate.of(2022, 11, 3), new Money(90, usdCurrency))
        );

        File actualFile = File.createTempFile("reflective-excel-writer", "fancy-table-test.xlsx");

        SerializationContext serializationContext = new DefaultSerializationContext();
        serializationContext.registerSerializer(Money.class, (context, value) -> value.getAmount());

        CellStyleContext styleContext = new DefaultStyleContext();
        styleContext.setHeaderStyleProvider(StyleProviders.of(WritableCellStyles.boldText()));
        styleContext.setCellStyleProvider(StyleProviders.of(WritableCellStyles.allSideBorder()));
        styleContext.setRowStyleProvider(StyleProviders.stripedRows(new StyleColor(240, 248, 255), null));
        styleContext.setTypeStyleProvider(Money.class, new StyleProvider() {

            private static final String CURRENCY_FORMAT = "_(\\%s* #,###_);_(\\%<s\\ * \\-#,###_);_(\\%<s* \"\"\\-\"\"??_);_(@_)";

            @Override
            public WritableCellStyle getStyle(WritingContext context, Object target) {
                Money money = (Money) target;
                return WritableCellStyle.builder()
                        .format(CURRENCY_FORMAT.formatted(money.getCurrencySymbol(Locale.US)))
                        .build();
            }
        });

        ExcelWritingSettings settings = ExcelWritingSettings.builder()
                .tableStartCellLocator((context, tableId) -> CellLocation.fromReference("C3"))
                .serializationContext(serializationContext)
                .cellStyleContext(styleContext)
                .build();

        ReflectiveExcelWriter writer = new ReflectiveExcelWriter(actualFile, settings);
        writer.writeRows(rows, FoodExpensesRow.class);

        try (InputStream expectedInputStream = getClass().getResourceAsStream("/test-styled-table.xlsx")) {
            ExcelAssert.assertThat(actualFile)
                    .isEqualTo(expectedInputStream);
        } finally {
            actualFile.delete();
        }
    }

    public static class FoodExpensesRow {

        @ExcelColumn("name")
        private String name;

        @ExcelColumn("date")
        private LocalDate date;

        @ExcelColumn("spent")
        private Money amount;

        public FoodExpensesRow() {
        }

        public FoodExpensesRow(String name, LocalDate date, Money amount) {
            this.name = name;
            this.date = date;
            this.amount = amount;
        }

        public static class Money {

            private final double amount;
            private final Currency currency;

            public Money(double amount, Currency currency) {
                this.amount = amount;
                this.currency = currency;
            }

            public double getAmount() {
                return amount;
            }

            public String getCurrencySymbol() {
                return currency.getSymbol();
            }

            public String getCurrencySymbol(Locale locale) {
                return currency.getSymbol(locale);
            }
        }
    }
}
