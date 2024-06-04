package com.mihai.writer.writers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mihai.core.annotation.ExcelCellValue;
import com.mihai.core.annotation.ExcelProperty;
import com.mihai.core.annotation.TableId;
import com.mihai.writer.ExcelWritingSettings;
import com.mihai.writer.ExcelWritingTest;
import com.mihai.writer.WritableSheet;
import com.mihai.writer.WritableSheetContext;
import com.mihai.writer.serializer.DefaultSerializationContext;
import com.mihai.writer.style.DefaultStyleContext;
import org.junit.jupiter.api.Test;

public class ObjectWriterTest extends ExcelWritingTest {

    @Test
    public void objectCellValuesAreWrittenToSheet() {
        WritableSheetContext sheetContext = createSheetContext();
        ObjectWriter writer = new ObjectWriter(getWritableSheet(), sheetContext, ExcelWritingSettings.DEFAULT);

        DummyValue dummyValue = new DummyValue();
        dummyValue.value = "abc";

        writer.write(dummyValue);

        String cellValue = getCell(0, 0).getStringCellValue();
        assertEquals("abc", cellValue);
    }

    @Test
    public void objectPropertyValuesAreWrittenToSheet() {
        WritableSheetContext sheetContext = createSheetContext();
        ObjectWriter writer = new ObjectWriter(getWritableSheet(), sheetContext, ExcelWritingSettings.DEFAULT);

        DummyPropertyValue propertyValue = new DummyPropertyValue();
        propertyValue.value = "abc";

        writer.write(propertyValue);

        String property = getCell(0, 0).getStringCellValue();
        String value = getCell(0, 1).getStringCellValue();
        assertEquals("property", property);
        assertEquals("abc", value);
    }

    @Test
    public void annotatingNonListAsTableIdThrownException() {
        WritableSheetContext sheetContext = createSheetContext();
        ObjectWriter writer = new ObjectWriter(getWritableSheet(), sheetContext, ExcelWritingSettings.DEFAULT);

        WrongTableIdAnnotatedType table = new WrongTableIdAnnotatedType();

        assertThrows(IllegalStateException.class, () -> writer.write(table));
    }

    public static class DummyValue {

        @ExcelCellValue(cellReference = "A1")
        private String value;
    }

    public static class DummyPropertyValue {

        @ExcelProperty(name = "property", nameReference = "A1", valueReference = "B1")
        private String value;
    }

    public static class WrongTableIdAnnotatedType {

        @TableId("table")
        private String table;
    }
}
