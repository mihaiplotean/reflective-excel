package com.reflectiveexcel.reader.deserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.reflectiveexcel.reader.ReadingContext;
import com.reflectiveexcel.reader.exception.BadInputException;
import com.reflectiveexcel.reader.workbook.sheet.ReadableCell;

/**
 * Deserializes a local date time string representation to a {@link LocalDateTime}, using the provided date format.
 */
public class LocalDateTimeDeserializer implements CellDeserializer<LocalDateTime> {

    private final DateTimeFormatter dateTimeFormatter;

    public LocalDateTimeDeserializer(String datePattern) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
    }

    @Override
    public LocalDateTime deserialize(ReadingContext context, ReadableCell cell) throws BadInputException {
        String date = cell.getValue();
        try {
            return LocalDateTime.parse(date, dateTimeFormatter);
        } catch (DateTimeParseException ex) {
            throw new BadInputException(String.format(
                    "Value \"%s\" defined in cell %s is not a date that matches the specified format", date, cell.getCellReference()
            ));
        }
    }
}
