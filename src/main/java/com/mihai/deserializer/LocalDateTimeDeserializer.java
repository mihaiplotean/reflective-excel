package com.mihai.deserializer;

import com.mihai.ReadingContext;
import com.mihai.exception.BadInputException;
import com.mihai.workbook.sheet.PropertyCell;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeDeserializer implements CellDeserializer<LocalDateTime> {

    private final DateTimeFormatter dateTimeFormatter;

    public LocalDateTimeDeserializer(String datePattern) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
    }

    @Override
    public LocalDateTime deserialize(ReadingContext context, PropertyCell cell) throws BadInputException {
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
