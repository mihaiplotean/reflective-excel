package com.mihai.deserializer;

import com.mihai.exception.BadInputException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Date;

public class CellDeserializers {

    private CellDeserializers() {
        throw new IllegalStateException("Utility class");
    }

    public static CellDeserializer<String> forString() {
        return (context, cell) -> cell.getValue();
    }

    public static CellDeserializer<Integer> forInteger() {
        return (context, cell) -> {
            String cellValue = cell.getValue();
            try {
                return Integer.parseInt(cellValue);
            } catch (NumberFormatException ex) {
                throw new BadInputException(String.format(
                        "Value \"%s\" defined in cell %s is not an integer", cellValue, cell.getCellReference()
                ));
            }
        };
    }

    public static CellDeserializer<Long> forLong() {
        return (context, cell) -> {
            String cellValue = cell.getValue();
            try {
                return Long.parseLong(cellValue);
            } catch (NumberFormatException ex) {
                throw new BadInputException(String.format(
                        "Value \"%s\" defined in cell %s is not a long", cellValue, cell.getCellReference()
                ));
            }
        };
    }

    public static CellDeserializer<Double> forDouble() {
        return (context, cell) -> {
            String cellValue = cell.getValue();
            try {
                return Double.parseDouble(cellValue);
            } catch (NumberFormatException ex) {
                throw new BadInputException(String.format(
                        "Value \"%s\" defined in cell %s is not a decimal", cellValue, cell.getCellReference()
                ));
            }
        };
    }

    public static CellDeserializer<Float> forFloat() {
        return (context, cell) -> {
            String cellValue = cell.getValue();
            try {
                return Float.parseFloat(cellValue);
            } catch (NumberFormatException ex) {
                throw new BadInputException(String.format(
                        "Value \"%s\" defined in cell %s is not a float", cellValue, cell.getCellReference()
                ));
            }
        };
    }

    public static CellDeserializer<Boolean> forBoolean() {
        return (context, cell) -> {
            String cellValue = cell.getValue();
            if (cellValue.equalsIgnoreCase("true")) {
                return true;
            }
            if (cellValue.equalsIgnoreCase("false")) {
                return false;
            }
            throw new BadInputException(String.format(
                    "Value \"%s\" defined in cell %s is not \"true\" or \"false\"", cellValue, cell.getCellReference()
            ));
        };
    }

    public static CellDeserializer<Currency> forCurrency() {
        return new CurrencyDeserializer();
    }

    public static <E extends Enum<E>> CellDeserializer<E> forEnum(Class<E> enumClazz) {
        return (context, cell) -> {
            String cellValue = cell.getValue();
            for (E enumConstant : enumClazz.getEnumConstants()) {
                if (enumConstant.toString().equalsIgnoreCase(cellValue)) {
                    return enumConstant;
                }
            }
            throw new BadInputException(String.format(
                    "Value \"%s\" defined in cell %s is not a known enum value", cellValue, cell.getCellReference()
            ));
        };
    }

    public static CellDeserializer<Date> forDate() {
        return (context, cell) -> {
            try {
                return cell.getDateValue();
            } catch (IllegalStateException | NumberFormatException e) {
                throw new BadInputException(String.format(
                        "Value \"%s\" defined in cell %s does not have a date number format", cell.getValue(), cell.getCellReference()
                ));
            }
        };
    }

    public static CellDeserializer<Date> forDate(String datePattern) {
        return new DateDeserializer(datePattern);
    }

    public static CellDeserializer<LocalDateTime> forLocalDateTime() {
        return (context, cell) -> {
            try {
                return cell.getLocalDateTimeValue();
            } catch (IllegalStateException | NumberFormatException e) {
                throw new BadInputException(String.format(
                        "Value \"%s\" defined in cell %s does not have a date number format", cell.getValue(), cell.getCellReference()
                ));
            }
        };
    }

    public static CellDeserializer<LocalDateTime> forLocalDateTime(String datePattern) {
        return new LocalDateTimeDeserializer(datePattern);
    }

    public static CellDeserializer<LocalDate> forLocalDate() {
        CellDeserializer<LocalDateTime> dateTimeCellDeserializer = forLocalDateTime();
        return (context, cell) -> dateTimeCellDeserializer.deserialize(context, cell).toLocalDate();
    }

    public static CellDeserializer<LocalDate> forLocalDate(String datePattern) {
        return new LocalDateDeserializer(datePattern);
    }
}
