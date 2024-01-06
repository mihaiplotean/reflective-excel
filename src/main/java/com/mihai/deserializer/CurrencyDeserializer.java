package com.mihai.deserializer;

import com.mihai.ReadingContext;
import com.mihai.exception.BadInputException;
import com.mihai.workbook.PropertyCell;

import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public class CurrencyDeserializer implements CellDeserializer<Currency> {

    private static final Set<String> KNOWN_CURRENCIES = getKnownCurrencyCodes();

    private static Set<String> getKnownCurrencyCodes() {
        return Currency.getAvailableCurrencies().stream()
                .map(Currency::getCurrencyCode)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Currency deserialize(ReadingContext context, PropertyCell cell) throws BadInputException {
        String value = cell.getValue();
        if (KNOWN_CURRENCIES.contains(value)) {
            return Currency.getInstance(value);
        }
        throw new BadInputException(String.format(
                "Currency %s defined in cell %s is not known", value, cell.getCellReference()
        ));
    }
}
