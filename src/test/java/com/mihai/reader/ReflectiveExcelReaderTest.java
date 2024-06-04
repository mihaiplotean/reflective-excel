package com.mihai.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.mihai.core.ReflectiveExcelException;
import com.mihai.core.annotation.ExcelCellValue;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

public class ReflectiveExcelReaderTest {

    @Test
    public void readingWorkbookWithoutSheetsReturnsEmptyList() throws IOException {
        File tempFile = File.createTempFile("empty-workbook", "test");
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            workbook.write(outputStream);

            List<ReflectiveExcelReader> rows = new ReflectiveExcelReader(tempFile).readRows(ReflectiveExcelReader.class);
            assertEquals(List.of(), rows);
        }
    }

    @Test
    public void readingSheetByProvingNegativeIndexReadsSheetFromTheEnd() throws IOException {
        File tempFile = File.createTempFile("negative-index", "test");
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            workbook.createSheet("Sheet A");
            workbook.createSheet("Sheet B")
                    .createRow(0)
                    .createCell(0).setCellValue(42);
            workbook.write(outputStream);

            ExcelReadingSettings settings = ExcelReadingSettings.builder()
                    .sheetIndex(-1)
                    .build();
            DummyValue value = new ReflectiveExcelReader(tempFile, settings).read(DummyValue.class);
            assertEquals(42, value.value);
        }
    }

    @Test
    public void readingSheetByNameReadsCorrectSheet() throws IOException {
        File tempFile = File.createTempFile("negative-index", "test");
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            workbook.createSheet("Sheet A");
            workbook.createSheet("Sheet B")
                    .createRow(0)
                    .createCell(0).setCellValue(42);
            workbook.write(outputStream);

            ExcelReadingSettings settings = ExcelReadingSettings.builder()
                    .sheetName("Sheet B")
                    .build();
            DummyValue value = new ReflectiveExcelReader(tempFile, settings).read(DummyValue.class);
            assertEquals(42, value.value);
        }
    }

    @Test
    public void readingInvalidStreamThrowsException() throws IOException {
        try (InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Could not read input stream!");
            }
        }) {
            assertThrows(ReflectiveExcelException.class, () -> new ReflectiveExcelReader(inputStream).read(DummyValue.class));
        }
    }

    @Test
    public void readingInvalidStreamThrowsException2() throws IOException {
        try (InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Could not read input stream!");
            }
        }) {
            assertThrows(ReflectiveExcelException.class, () -> new ReflectiveExcelReader(inputStream).readRows(DummyValue.class));
        }
    }

    public static class DummyValue {

        @ExcelCellValue(cellReference = "A1")
        private int value;
    }
}
