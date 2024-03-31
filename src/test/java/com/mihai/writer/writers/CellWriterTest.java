package com.mihai.writer.writers;

import com.mihai.writer.WritableCell;
import com.mihai.writer.WritableSheet;
import com.mihai.writer.locator.CellLocation;
import com.mihai.writer.style.WritableCellStyle;
import com.mihai.writer.style.WritableCellStyles;
import com.mihai.writer.style.color.CellColor;
import com.mihai.writer.writers.CellWriter;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CellWriterTest {

    private XSSFWorkbook workbook;
    private XSSFSheet actualSheet;
    private WritableSheet sheet;

    @BeforeEach
    public void setUp() {
        workbook = new XSSFWorkbook();
        actualSheet = workbook.createSheet();
        sheet = new WritableSheet(actualSheet);
    }

    @AfterEach
    public void tearDown() throws IOException {
        workbook.close();
    }

    @Test
    public void cellIsCreated() {
        CellWriter cellWriter = new CellWriter(sheet);
        cellWriter.writeCell(new WritableCell("test", 1, 1), List.of());

        XSSFCell cell = actualSheet.getRow(1).getCell(1);

        assertNotNull(cell);
        assertEquals("test", cell.getStringCellValue());
    }

    @Test
    public void regionSpawningMultipleCellsIsCreated() {
        CellWriter cellWriter = new CellWriter(sheet);

        cellWriter.writeCell(new WritableCell("test", 0, 1, 3, 4), List.of());

        CellRangeAddress mergedRegion = actualSheet.getMergedRegion(0);
        assertEquals(0, mergedRegion.getFirstRow());
        assertEquals(1, mergedRegion.getFirstColumn());
        assertEquals(3, mergedRegion.getLastRow());
        assertEquals(4, mergedRegion.getLastColumn());
    }

    @Test
    public void styleIsAppliedToCreatedCell() {
        CellWriter cellWriter = new CellWriter(sheet);
        cellWriter.writeCell(new WritableCell("", 1, 1), List.of(WritableCellStyles.format("abc")));

        assertEquals("abc", actualSheet.getRow(1).getCell(1).getCellStyle().getDataFormatString());
    }

    @Test
    public void stylesAreMerged() {
        CellWriter cellWriter = new CellWriter(sheet);
        List<WritableCellStyle> styles = List.of(
                WritableCellStyles.format("abc"),
                WritableCellStyle.builder()
                        .format("a")
                        .backgroundColor(new CellColor(1, 2, 3))
                        .build()
        );
        cellWriter.writeCell(new WritableCell("", 1, 1), styles);

        XSSFCellStyle cellStyle = actualSheet.getRow(1).getCell(1).getCellStyle();
        assertEquals("abc", cellStyle.getDataFormatString());
        assertArrayEquals(new byte[]{1, 2, 3}, cellStyle.getFillForegroundColorColor().getRGB());
    }

    @Test
    public void cellOffsetIsApplied() {
        CellWriter cellWriter = new CellWriter(sheet);
        cellWriter.setOffSet(2, 2);

        CellLocation cellLocation = cellWriter.writeCell(new WritableCell("", 1, 1), List.of());

        assertEquals(3, cellLocation.getRow());
        assertEquals(3, cellLocation.getColumn());
        assertNotNull(actualSheet.getRow(3).getCell(3));
    }
}
