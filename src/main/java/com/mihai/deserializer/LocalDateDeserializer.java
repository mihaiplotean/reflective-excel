package com.mihai.deserializer;

import com.mihai.ReadingContext;
import com.mihai.exception.BadInputException;
import com.mihai.workbook.PropertyCell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateDeserializer implements CellDeserializer<LocalDate> {

    private final DateTimeFormatter dateTimeFormatter;

    public LocalDateDeserializer(String datePattern) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
    }

    @Override
    public LocalDate deserialize(ReadingContext context, PropertyCell cell) throws BadInputException {
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
