package com.mihai.writer.writers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import com.mihai.core.annotation.ExcelCellValue;
import com.mihai.core.annotation.ExcelProperty;
import com.mihai.core.annotation.TableId;
import com.mihai.writer.ExcelWritingSettings;
import com.mihai.writer.WritableSheet;
import com.mihai.writer.WritableSheetContext;
import com.mihai.writer.serializer.DefaultSerializationContext;
import com.mihai.writer.style.DefaultStyleContext;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObjectWriterTest {

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
    public void objectCellValuesAreWrittenToSheet() {
        WritableSheet sheet = new WritableSheet(actualSheet);
        WritableSheetContext sheetContext = new WritableSheetContext(new DefaultSerializationContext(), new DefaultStyleContext());
        ObjectWriter writer = new ObjectWriter(sheet, sheetContext, ExcelWritingSettings.DEFAULT);

        DummyValue dummyValue = new DummyValue();
        dummyValue.value = "abc";

        writer.write(dummyValue);

        String cellValue = actualSheet.getRow(0).getCell(0).getStringCellValue();
        assertEquals("abc", cellValue);
    }

    @Test
    public void objectPropertyValuesAreWrittenToSheet() {
        WritableSheet sheet = new WritableSheet(actualSheet);
        WritableSheetContext sheetContext = new WritableSheetContext(new DefaultSerializationContext(), new DefaultStyleContext());
        ObjectWriter writer = new ObjectWriter(sheet, sheetContext, ExcelWritingSettings.DEFAULT);

        DummyPropertyValue propertyValue = new DummyPropertyValue();
        propertyValue.value = "abc";

        writer.write(propertyValue);

        String property = actualSheet.getRow(0).getCell(0).getStringCellValue();
        String value = actualSheet.getRow(0).getCell(1).getStringCellValue();
        assertEquals("property", property);
        assertEquals("abc", value);
    }

    @Test
    public void annotatingNonListAsTableIdThrownException() {
        WritableSheet sheet = new WritableSheet(actualSheet);
        WritableSheetContext sheetContext = new WritableSheetContext(new DefaultSerializationContext(), new DefaultStyleContext());
        ObjectWriter writer = new ObjectWriter(sheet, sheetContext, ExcelWritingSettings.DEFAULT);

        WrongTableIdAnnotatedType table = new WrongTableIdAnnotatedType();

        assertThrows(IllegalStateException.class, () -> writer.write(table));
    }

    public class DummyValue {

        @ExcelCellValue(cellReference = "A1")
        private String value;
    }

    public class DummyPropertyValue {

        @ExcelProperty(name = "property", nameReference = "A1", valueReference = "B1")
        private String value;
    }

    public class WrongTableIdAnnotatedType {

        @TableId("table")
        private String table;
    }
}
