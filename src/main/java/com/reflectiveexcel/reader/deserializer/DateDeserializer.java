package com.reflectiveexcel.reader.deserializer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.reflectiveexcel.reader.ReadingContext;
import com.reflectiveexcel.reader.exception.BadInputException;
import com.reflectiveexcel.reader.workbook.sheet.ReadableCell;

/**
 * Deserializes a date string representation to a {@link Date}, using the provided date format.
 */
public class DateDeserializer implements CellDeserializer<Date> {

    private final DateFormat dateFormat;

    public DateDeserializer(String datePattern) {
        this.dateFormat = new SimpleDateFormat(datePattern);
    }

    @Override
    public Date deserialize(ReadingContext context, ReadableCell cell) throws BadInputException {
        String date = cell.getValue();
        try {
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            throw new BadInputException(String.format(
                    "Value \"%s\" defined in cell %s is not a date that matches the specified format", date, cell.getCellReference()
            ));
        }
    }
}
