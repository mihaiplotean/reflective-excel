package com.mihai.reader.deserializer;

import com.mihai.core.utils.CollectionUtilities;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.exception.BadInputException;
import com.mihai.reader.workbook.sheet.ReadableCell;

import java.util.Currency;
import java.util.Set;

public class CurrencyDeserializer implements CellDeserializer<Currency> {

    private static final Set<String> KNOWN_CURRENCIES = getKnownCurrencyCodes();

    private static Set<String> getKnownCurrencyCodes() {
        return Currency.getAvailableCurrencies().stream()
                .map(Currency::getCurrencyCode)
                .collect(CollectionUtilities.toCaseInsensitiveSetCollector());
    }

    @Override
    public Currency deserialize(ReadingContext context, ReadableCell cell) throws BadInputException {
        String value = cell.getValue();
        if (KNOWN_CURRENCIES.contains(value)) {
            return Currency.getInstance(value);
        }
        throw new BadInputException(String.format(
                "Currency %s defined in cell %s is not known", value, cell.getCellReference()
        ));
    }
}
