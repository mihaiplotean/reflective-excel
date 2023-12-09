package com.mihai.deserializer;

import com.mihai.ExcelCell;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Date;

public class CellDeserializers {

    private CellDeserializers() {
        throw new IllegalStateException("Utility class");
    }

    public static CellDeserializer<String> forString() {
        return ExcelCell::getValue;
    }

    public static CellDeserializer<Integer> forInteger() {
        return cellDetails -> {
            String cellValue = cellDetails.getValue();
            try {
                return Integer.parseInt(cellValue);
            } catch (NumberFormatException ex) {
                throw new DeserializationFailedException(String.format(
                        "Value \"%s\" defined in cell %s is not an integer", cellValue, cellDetails.getCellReference()
                ));
            }
        };
    }

    public static CellDeserializer<Long> forLong() {
        return cellDetails -> {
            String cellValue = cellDetails.getValue();
            try {
                return Long.parseLong(cellValue);
            } catch (NumberFormatException ex) {
                throw new DeserializationFailedException(String.format(
                        "Value \"%s\" defined in cell %s is not a long", cellValue, cellDetails.getCellReference()
                ));
            }
        };
    }

    public static CellDeserializer<Double> forDouble() {
        return cellDetails -> {
            String cellValue = cellDetails.getValue();
            try {
                return Double.parseDouble(cellValue);
            } catch (NumberFormatException ex) {
                throw new DeserializationFailedException(String.format(
                        "Value \"%s\" defined in cell %s is not a decimal", cellValue, cellDetails.getCellReference()
                ));
            }
        };
    }

    public static CellDeserializer<Float> forFloat() {
        return cellDetails -> {
            String cellValue = cellDetails.getValue();
            try {
                return Float.parseFloat(cellValue);
            } catch (NumberFormatException ex) {
                throw new DeserializationFailedException(String.format(
                        "Value \"%s\" defined in cell %s is not a float", cellValue, cellDetails.getCellReference()
                ));
            }
        };
    }

    public static CellDeserializer<Boolean> forBoolean() {
        return cellDetails -> {
            String cellValue = cellDetails.getValue();
            if (cellValue.equalsIgnoreCase("true")) {
                return true;
            }
            if (cellValue.equalsIgnoreCase("false")) {
                return false;
            }
            throw new DeserializationFailedException(String.format(
                    "Value \"%s\" defined in cell %s is not \"true\" or \"false\"", cellValue, cellDetails.getCellReference()
            ));
        };
    }

    public static CellDeserializer<Currency> forCurrency() {
        return new CurrencyDeserializer();
    }

    public static <E extends Enum<E>> CellDeserializer<E> forEnum(Class<E> enumClazz) {
        return cellDetails -> {
            String cellValue = cellDetails.getValue();
            for (E enumConstant : enumClazz.getEnumConstants()) {
                if (enumConstant.toString().equalsIgnoreCase(cellValue)) {
                    return enumConstant;
                }
            }
            throw new DeserializationFailedException(String.format(
                    "Value \"%s\" defined in cell %s is not a known enum value", cellValue, cellDetails.getCellReference()
            ));
        };
    }

    public static CellDeserializer<Date> forDate() {
        return cellDetails -> {
            try {
                return cellDetails.getDateValue();
            } catch (IllegalStateException | NumberFormatException e) {
                throw new DeserializationFailedException(String.format(
                        "Value \"%s\" defined in cell %s does not have a date number format", cellDetails.getValue(), cellDetails.getCellReference()
                ));
            }
        };
    }

    public static CellDeserializer<LocalDateTime> forLocalDateTime() {
        return cellDetails -> {
            try {
                return cellDetails.getLocalDateTimeValue();
            } catch (IllegalStateException | NumberFormatException e) {
                throw new DeserializationFailedException(String.format(
                        "Value \"%s\" defined in cell %s does not have a date number format", cellDetails.getValue(), cellDetails.getCellReference()
                ));
            }
        };
    }
}
