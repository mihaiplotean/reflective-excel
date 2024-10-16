package com.reflectiveexcel.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.junit.jupiter.api.Test;

public class WritableCellTest extends ExcelWritingTest {

    @Test
    public void stringValueAppliedToCell() {
        WritableCell cell = new WritableCell("string value", 0, 0);
        Cell actualCell = getSheet().createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.STRING, actualCell.getCellType());
        assertEquals("string value", actualCell.getStringCellValue());
    }

    @Test
    public void intValueAppliedToCell() {
        WritableCell cell = new WritableCell(42, 0, 0);
        Cell actualCell = getSheet().createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.NUMERIC, actualCell.getCellType());
        assertEquals(42, actualCell.getNumericCellValue());
    }

    @Test
    public void doubleValueAppliedToCell() {
        WritableCell cell = new WritableCell(42.2, 0, 0);
        Cell actualCell = getSheet().createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.NUMERIC, actualCell.getCellType());
        assertEquals(42.2, actualCell.getNumericCellValue());
    }

    @Test
    public void booleanValueAppliedToCell() {
        WritableCell cell = new WritableCell(true, 0, 0);
        Cell actualCell = getSheet().createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.BOOLEAN, actualCell.getCellType());
        assertTrue(actualCell.getBooleanCellValue());
    }

    @Test
    public void dateValueAppliedToCell() {
        Date date = new Date();
        WritableCell cell = new WritableCell(date, 0, 0);
        Cell actualCell = getSheet().createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.NUMERIC, actualCell.getCellType());
        assertEquals(date, actualCell.getDateCellValue());
    }

    @Test
    public void localDateTimeValueAppliedToCell() {
        LocalDateTime dateTime = LocalDateTime.of(2020, 1, 1, 1, 1);
        WritableCell cell = new WritableCell(dateTime, 0, 0);
        Cell actualCell = getSheet().createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.NUMERIC, actualCell.getCellType());
        assertEquals(dateTime, actualCell.getLocalDateTimeCellValue());
    }

    @Test
    public void localDateValueAppliedToCell() {
        LocalDate date = LocalDate.of(2020, 1, 1);
        WritableCell cell = new WritableCell(date, 0, 0);
        Cell actualCell = getSheet().createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.NUMERIC, actualCell.getCellType());
        assertEquals(date.atTime(0, 0), actualCell.getLocalDateTimeCellValue());
    }

    @Test
    public void calendarValueAppliedToCell() {
        Calendar calendar = Calendar.getInstance();
        WritableCell cell = new WritableCell(calendar, 0, 0);
        Cell actualCell = getSheet().createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.NUMERIC, actualCell.getCellType());
        assertEquals(calendar.getTime(), actualCell.getDateCellValue());
    }

    @Test
    public void bigIntegerValueAppliedToCell() {
        WritableCell cell = new WritableCell(BigInteger.valueOf(12), 0, 0);
        Cell actualCell = getSheet().createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.NUMERIC, actualCell.getCellType());
        assertEquals(12, actualCell.getNumericCellValue());
    }

    @Test
    public void bigDecimalValueAppliedToCell() {
        WritableCell cell = new WritableCell(BigDecimal.valueOf(12d), 0, 0);
        Cell actualCell = getSheet().createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.NUMERIC, actualCell.getCellType());
        assertEquals(12d, actualCell.getNumericCellValue());
    }
}
