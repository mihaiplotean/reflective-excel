package com.mihai.reader.deserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.mihai.reader.ReadingContext;
import com.mihai.reader.exception.BadInputException;
import com.mihai.reader.workbook.sheet.ReadableCell;

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
