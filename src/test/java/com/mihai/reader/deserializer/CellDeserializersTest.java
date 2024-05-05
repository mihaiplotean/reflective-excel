package com.mihai.reader.deserializer;

import com.mihai.common.field.AnnotatedFieldType;
import com.mihai.reader.exception.BadInputException;
import com.mihai.reader.workbook.sheet.ReadableSheet;
import com.mihai.writer.style.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Date;

class CellDeserializersTest {

    private XSSFWorkbook workbook;
    private XSSFSheet actualSheet;
    private ReadableSheet sheet;

    @BeforeEach
    public void setUp() {
        workbook = new XSSFWorkbook();
        actualSheet = workbook.createSheet();
        sheet = new ReadableSheet(actualSheet);
    }

    @AfterEach
    public void tearDown() throws IOException {
        workbook.close();
    }

    @Test
    public void stringDeserializationReturnsDefinedCellValue() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue("test value");
        String cellValue = CellDeserializers.forString().deserialize(null, sheet.getCell(0, 0));
        assertEquals("test value", cellValue);
    }

    @Test
    public void numberDeserializationReturnsCorrectInteger() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue(42d);
        Integer cellValue = CellDeserializers.forInteger().deserialize(null, sheet.getCell(0, 0));
        assertEquals(42, cellValue);
    }

    @Test
    public void numberDeserializationReturnsCorrectDouble() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue(42.2);
        Double cellValue = CellDeserializers.forDouble().deserialize(null, sheet.getCell(0, 0));
        assertEquals(42.2, cellValue);
    }

    @Test
    public void booleanDeserializationReturnsCorrectBoolean() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue(true);
        Boolean cellValue = CellDeserializers.forBoolean().deserialize(null, sheet.getCell(0, 0));
        assertEquals(true, cellValue);
    }

    @Test
    public void invalidBooleanValueDeserializationThrowsException() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue("not a boolean");
        assertThrows(BadInputException.class, () -> CellDeserializers.forBoolean().deserialize(null, sheet.getCell(0, 0)));
    }

    @Test
    public void currencyDeserializationReturnsCorrectCurrency() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue("EUR");
        Currency cellValue = CellDeserializers.forCurrency().deserialize(null, sheet.getCell(0, 0));
        assertEquals(Currency.getInstance("EUR"), cellValue);
    }

    @Test
    public void invalidCurrencyDeserializationThrowsException() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue("invalid currency");
        assertThrows(BadInputException.class, () -> CellDeserializers.forCurrency().deserialize(null, sheet.getCell(0, 0)));
    }

    @Test
    public void enumDeserializationReturnsCorrespondingEnumBasedOnToString() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue("CELL_VALUE");
        assertEquals(AnnotatedFieldType.CELL_VALUE,
                CellDeserializers.forEnum(AnnotatedFieldType.class).deserialize(null, sheet.getCell(0, 0)));
    }

    @Test
    public void invalidEnumDeserializationThrowsException() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue("UNKNOWN_VALUE");
        assertThrows(BadInputException.class,
                () -> CellDeserializers.forEnum(AnnotatedFieldType.class).deserialize(null, sheet.getCell(0, 0)));
    }

    @Test
    public void dateDeserializationReturnsCorrectDate() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        Date date = new Date();
        cell.setCellValue(date);
        assertEquals(date, CellDeserializers.forDate().deserialize(null, sheet.getCell(0, 0)));
    }

    @Test
    public void dateBasedOnPatternDeserializationReturnsCorrectDate() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue("14-05-2024");
        assertEquals(DateFormatUtils.createDate(14, 5, 2024),
                CellDeserializers.forDate("dd-MM-yyyy").deserialize(null, sheet.getCell(0, 0)));
    }

    @Test
    public void invalidDateDeserializationThrowsException() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue("not a date");
        assertThrows(BadInputException.class,
                () -> CellDeserializers.forDate().deserialize(null, sheet.getCell(0, 0)));
    }

    @Test
    public void localDateDeserializationReturnsCorrectLocalDate() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        LocalDate localDate = LocalDate.of(2024, 5, 14);
        cell.setCellValue(localDate);
        assertEquals(localDate, CellDeserializers.forLocalDate().deserialize(null, sheet.getCell(0, 0)));
    }

    @Test
    public void localDateBasedOnPatternDeserializationReturnsCorrectLocalDate() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue("14-05-2024");
        assertEquals(LocalDate.of(2024, 5, 14),
                CellDeserializers.forLocalDate("dd-MM-yyyy").deserialize(null, sheet.getCell(0, 0)));
    }

    @Test
    public void localDateTimeDeserializationReturnsCorrectLocalDateTime() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        LocalDateTime localDate = LocalDateTime.of(2024, 5, 14, 8, 33);
        cell.setCellValue(localDate);
        assertEquals(localDate, CellDeserializers.forLocalDateTime().deserialize(null, sheet.getCell(0, 0)));
    }

    @Test
    public void localDateTimeBasedOnPatternDeserializationReturnsCorrectLocalDateTime() {
        Cell cell = actualSheet.createRow(0).createCell(0);
        cell.setCellValue("14-05-2024 08:33");
        assertEquals(LocalDateTime.of(2024, 5, 14, 8, 33),
                CellDeserializers.forLocalDateTime("dd-MM-yyyy HH:mm").deserialize(null, sheet.getCell(0, 0)));
    }
}
