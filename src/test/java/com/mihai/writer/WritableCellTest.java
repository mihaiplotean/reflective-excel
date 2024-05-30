package com.mihai.writer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class WritableCellTest {

    private XSSFWorkbook workbook;
    private XSSFSheet actualSheet;

    @BeforeEach
    public void setUp() {
        workbook = new XSSFWorkbook();
        actualSheet = workbook.createSheet();
    }

    @AfterEach
    public void tearDown() throws IOException {
        workbook.close();
    }

    @Test
    public void stringValueAppliedToCell() {
        WritableCell cell = new WritableCell("string value", 0, 0);
        Cell actualCell = actualSheet.createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.STRING, actualCell.getCellType());
        assertEquals("string value", actualCell.getStringCellValue());
    }

    @Test
    public void intValueAppliedToCell() {
        WritableCell cell = new WritableCell(42, 0, 0);
        Cell actualCell = actualSheet.createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.NUMERIC, actualCell.getCellType());
        assertEquals(42, actualCell.getNumericCellValue());
    }

    @Test
    public void doubleValueAppliedToCell() {
        WritableCell cell = new WritableCell(42.2, 0, 0);
        Cell actualCell = actualSheet.createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.NUMERIC, actualCell.getCellType());
        assertEquals(42.2, actualCell.getNumericCellValue());
    }

    @Test
    public void booleanValueAppliedToCell() {
        WritableCell cell = new WritableCell(true, 0, 0);
        Cell actualCell = actualSheet.createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.BOOLEAN, actualCell.getCellType());
        assertTrue(actualCell.getBooleanCellValue());
    }

    @Test
    public void dateValueAppliedToCell() {
        Date date = new Date();
        WritableCell cell = new WritableCell(date, 0, 0);
        Cell actualCell = actualSheet.createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.NUMERIC, actualCell.getCellType());
        assertEquals(date, actualCell.getDateCellValue());
    }

    @Test
    public void localDateTimeValueAppliedToCell() {
        LocalDateTime dateTime = LocalDateTime.of(2020, 1, 1, 1, 1);
        WritableCell cell = new WritableCell(dateTime, 0, 0);
        Cell actualCell = actualSheet.createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.NUMERIC, actualCell.getCellType());
        assertEquals(dateTime, actualCell.getLocalDateTimeCellValue());
    }

    @Test
    public void localDateValueAppliedToCell() {
        LocalDate date = LocalDate.of(2020, 1, 1);
        WritableCell cell = new WritableCell(date, 0, 0);
        Cell actualCell = actualSheet.createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.NUMERIC, actualCell.getCellType());
        assertEquals(date.atTime(0, 0), actualCell.getLocalDateTimeCellValue());
    }

    @Test
    public void calendarValueAppliedToCell() {
        Calendar calendar = Calendar.getInstance();
        WritableCell cell = new WritableCell(calendar, 0, 0);
        Cell actualCell = actualSheet.createRow(0).createCell(0);
        cell.writeTo(actualCell);

        assertEquals(CellType.NUMERIC, actualCell.getCellType());
        assertEquals(calendar.getTime(), actualCell.getDateCellValue());
    }
}
