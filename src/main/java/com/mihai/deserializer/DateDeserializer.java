package com.mihai.deserializer;

import com.mihai.ExcelCell;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDeserializer implements CellDeserializer<Date> {

    private final DateFormat dateFormat;

    public DateDeserializer(String datePattern) {
        this.dateFormat = new SimpleDateFormat(datePattern);
    }

    @Override
    public Date deserialize(ExcelCell excelCell) throws DeserializationFailedException {
        String date = excelCell.getValue();
        try {
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            throw new DeserializationFailedException(String.format(
                    "Value \"%s\" defined in cell %s is not a date that matches the specified format", date, excelCell.getCellReference()
            ));
        }
    }
}
