package com.reflectiveexcel.reader.deserializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.reflectiveexcel.reader.ReadingContext;
import com.reflectiveexcel.reader.exception.BadInputException;
import com.reflectiveexcel.reader.workbook.sheet.ReadableCell;

/**
 * Deserializes a local date string representation to a {@link LocalDate}, using the provided date format.
 */
public class LocalDateDeserializer implements CellDeserializer<LocalDate> {

    private final DateTimeFormatter dateTimeFormatter;

    public LocalDateDeserializer(String datePattern) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
    }

    @Override
    public LocalDate deserialize(ReadingContext context, ReadableCell cell) throws BadInputException {
        String date = cell.getValue();
        try {
            return LocalDate.parse(date, dateTimeFormatter);
        } catch (DateTimeParseException ex) {
            throw new BadInputException(String.format(
                    "Value \"%s\" defined in cell %s is not a date that matches the specified format", date, cell.getCellReference()
            ));
        }
    }
}
