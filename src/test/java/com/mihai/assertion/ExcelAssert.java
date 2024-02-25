package com.mihai.assertion;

import com.mihai.reader.workbook.creation.WorkbookCreator;
import com.mihai.reader.workbook.creation.WorkbookFromFileCreator;
import com.mihai.reader.workbook.creation.WorkbookFromInputStreamCreator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class ExcelAssert {

    private final WorkbookCreator workbookCreator;
    private final ExcelAssertSettings settings;

    private ExcelAssert(WorkbookCreator workbookCreator, ExcelAssertSettings settings) {
        this.workbookCreator = workbookCreator;
        this.settings = settings;
    }

    public static ExcelAssert assertThat(InputStream inputStream) {
        return assertThat(inputStream, ExcelAssertSettings.DEFAULT);
    }

    public static ExcelAssert assertThat(InputStream inputStream, ExcelAssertSettings settings) {
        return new ExcelAssert(new WorkbookFromInputStreamCreator(inputStream), settings);
    }

    public static ExcelAssert assertThat(File file) {
        return assertThat(file, ExcelAssertSettings.DEFAULT);
    }

    public static ExcelAssert assertThat(File file, ExcelAssertSettings settings) {
        return new ExcelAssert(new WorkbookFromFileCreator(file), settings);
    }

    public void isEqualTo(InputStream inputStream) throws IOException {
        assertEqualWorkbooks(new WorkbookFromInputStreamCreator(inputStream));
    }

    public void isEqualTo(File file) throws IOException {
        assertEqualWorkbooks(new WorkbookFromFileCreator(file));
    }

    private void assertEqualWorkbooks(WorkbookCreator workbookCreator) throws IOException {
        try (Workbook workbookA = this.workbookCreator.create();
             Workbook workbookB = workbookCreator.create()) {
            for (String sheetName : settings.getSheetNames()) {
                Sheet sheetA = workbookA.getSheet(sheetName);
                Assertions.assertNotNull(sheetA, "Actual workbook is missing sheet " + sheetName);

                Sheet sheetB = workbookB.getSheet(sheetName);
                Assertions.assertNotNull(sheetB, "Expected workbook is missing sheet " + sheetName);

                new SheetAssert(sheetA, sheetB, settings).assertEqualSheets();
            }
        }
    }
}
