package com.mihai.integration.writer;

import java.util.Currency;
import java.util.Locale;

public class Money {

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
