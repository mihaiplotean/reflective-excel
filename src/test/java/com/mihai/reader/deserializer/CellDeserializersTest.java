package com.mihai.reader.deserializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Date;

import com.mihai.core.field.AnnotatedFieldType;
import com.mihai.core.utils.DateFormatUtils;
import com.mihai.reader.ExcelReadingTest;
import com.mihai.reader.exception.BadInputException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.junit.jupiter.api.Test;

public class CellDeserializersTest extends ExcelReadingTest {

    @Test
    public void stringDeserializationReturnsDefinedCellValue() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue("test value");
        String cellValue = CellDeserializers.forString().deserialize(null, getReadableCell(0, 0));
        assertEquals("test value", cellValue);
    }

    @Test
    public void numberDeserializationReturnsCorrectInteger() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue(42d);
        Integer cellValue = CellDeserializers.forInteger().deserialize(null, getReadableCell(0, 0));
        assertEquals(42, cellValue);
    }

    @Test
    public void numberDeserializationOfStringValueReturnsCorrectInteger() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue("42");
        Integer cellValue = CellDeserializers.forInteger().deserialize(null, getReadableCell(0, 0));
        assertEquals(42, cellValue);
    }

    @Test
    public void numberDeserializationReturnsCorrectDouble() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue(42.2);
        Double cellValue = CellDeserializers.forDouble().deserialize(null, getReadableCell(0, 0));
        assertEquals(42.2, cellValue);
    }

    @Test
    public void numberDeserializationOfStringValueReturnsCorrectDouble() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue("42.2");
        Double cellValue = CellDeserializers.forDouble().deserialize(null, getReadableCell(0, 0));
        assertEquals(42.2, cellValue);
    }

    @Test
    public void numberDeserializationOfFormulaCellReturnsCorrectInteger() {
        createRow(0).createCell(0).setCellValue(1);
        createRow(1).createCell(0).setCellValue(2);

        Cell cell = createRow(2).createCell(0);
        cell.setCellFormula("SUM(A1,A2)");
        Double cellValue = CellDeserializers.forDouble().deserialize(null, getReadableCell(2, 0));
        assertEquals(3, cellValue);
    }

    @Test
    public void numberDeserializationWithNumberFormattingReturnsCorrectInteger() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue(42.2);

        CellStyle style = getWorkbook().createCellStyle();
        style.setDataFormat((short) 5);  // prepends a dollar sign to the value

        cell.setCellStyle(style);

        Double cellValue = CellDeserializers.forDouble().deserialize(null, getReadableCell(0, 0));
        assertEquals(42.2, cellValue);
    }

    @Test
    public void trueValueDeserializationReturnsCorrectBoolean() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue(true);
        Boolean cellValue = CellDeserializers.forBoolean().deserialize(null, getReadableCell(0, 0));
        assertEquals(true, cellValue);
    }

    @Test
    public void falseValueDeserializationReturnsCorrectBoolean() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue(false);
        Boolean cellValue = CellDeserializers.forBoolean().deserialize(null, getReadableCell(0, 0));
        assertEquals(false, cellValue);
    }

    @Test
    public void invalidBooleanValueDeserializationThrowsException() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue("not a boolean");
        assertThrows(BadInputException.class, () -> CellDeserializers.forBoolean().deserialize(null, getReadableCell(0, 0)));
    }

    @Test
    public void currencyDeserializationReturnsCorrectCurrency() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue("EUR");
        Currency cellValue = CellDeserializers.forCurrency().deserialize(null, getReadableCell(0, 0));
        assertEquals(Currency.getInstance("EUR"), cellValue);
    }

    @Test
    public void invalidCurrencyDeserializationThrowsException() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue("invalid currency");
        assertThrows(BadInputException.class, () -> CellDeserializers.forCurrency().deserialize(null, getReadableCell(0, 0)));
    }

    @Test
    public void enumDeserializationReturnsCorrespondingEnumBasedOnToString() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue("CELL_VALUE");
        assertEquals(AnnotatedFieldType.CELL_VALUE,
                     CellDeserializers.forEnum(AnnotatedFieldType.class).deserialize(null, getReadableCell(0, 0)));
    }

    @Test
    public void invalidEnumDeserializationThrowsException() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue("UNKNOWN_VALUE");
        assertThrows(BadInputException.class,
                     () -> CellDeserializers.forEnum(AnnotatedFieldType.class).deserialize(null, getReadableCell(0, 0)));
    }

    @Test
    public void dateDeserializationReturnsCorrectDate() {
        Cell cell = createRow(0).createCell(0);
        Date date = new Date();
        cell.setCellValue(date);
        assertEquals(date, CellDeserializers.forDate().deserialize(null, getReadableCell(0, 0)));
    }

    @Test
    public void dateBasedOnPatternDeserializationReturnsCorrectDate() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue("14-05-2024");
        assertEquals(DateFormatUtils.createDate(14, 5, 2024),
                     CellDeserializers.forDate("dd-MM-yyyy").deserialize(null, getReadableCell(0, 0)));
    }

    @Test
    public void invalidDateDeserializationThrowsException() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue("not a date");
        assertThrows(BadInputException.class,
                     () -> CellDeserializers.forDate().deserialize(null, getReadableCell(0, 0)));
    }

    @Test
    public void localDateDeserializationReturnsCorrectLocalDate() {
        Cell cell = createRow(0).createCell(0);
        LocalDate localDate = LocalDate.of(2024, 5, 14);
        cell.setCellValue(localDate);
        assertEquals(localDate, CellDeserializers.forLocalDate().deserialize(null, getReadableCell(0, 0)));
    }

    @Test
    public void localDateBasedOnPatternDeserializationReturnsCorrectLocalDate() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue("14-05-2024");
        assertEquals(LocalDate.of(2024, 5, 14),
                     CellDeserializers.forLocalDate("dd-MM-yyyy").deserialize(null, getReadableCell(0, 0)));
    }

    @Test
    public void localDateTimeDeserializationReturnsCorrectLocalDateTime() {
        Cell cell = createRow(0).createCell(0);
        LocalDateTime localDate = LocalDateTime.of(2024, 5, 14, 8, 33);
        cell.setCellValue(localDate);
        assertEquals(localDate, CellDeserializers.forLocalDateTime().deserialize(null, getReadableCell(0, 0)));
    }

    @Test
    public void localDateTimeBasedOnPatternDeserializationReturnsCorrectLocalDateTime() {
        Cell cell = createRow(0).createCell(0);
        cell.setCellValue("14-05-2024 08:33");
        assertEquals(LocalDateTime.of(2024, 5, 14, 8, 33),
                     CellDeserializers.forLocalDateTime("dd-MM-yyyy HH:mm").deserialize(null, getReadableCell(0, 0)));
    }
}
