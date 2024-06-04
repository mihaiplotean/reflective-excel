package com.mihai.writer.writers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.mihai.core.workbook.CellLocation;
import com.mihai.writer.ExcelWritingTest;
import com.mihai.writer.WritableCell;
import com.mihai.writer.style.WritableCellStyle;
import com.mihai.writer.style.WritableCellStyles;
import com.mihai.writer.style.color.StyleColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.junit.jupiter.api.Test;

public class CellWriterTest extends ExcelWritingTest {

    @Test
    public void cellIsCreated() {
        CellWriter cellWriter = createCellWriter();
        cellWriter.writeCell(new WritableCell("test", 1, 1), List.of());

        Cell cell = getCell(1, 1);

        assertNotNull(cell);
        assertEquals("test", cell.getStringCellValue());
    }

    @Test
    public void regionSpawningMultipleCellsIsCreated() {
        CellWriter cellWriter = createCellWriter();

        cellWriter.writeCell(new WritableCell("test", 0, 1, 3, 4), List.of());

        CellRangeAddress mergedRegion = getSheet().getMergedRegion(0);
        assertEquals(0, mergedRegion.getFirstRow());
        assertEquals(1, mergedRegion.getFirstColumn());
        assertEquals(3, mergedRegion.getLastRow());
        assertEquals(4, mergedRegion.getLastColumn());
    }

    @Test
    public void styleIsAppliedToCreatedCell() {
        CellWriter cellWriter = createCellWriter();
        cellWriter.writeCell(new WritableCell("", 1, 1), List.of(WritableCellStyles.format("abc")));

        assertEquals("abc", getCell(1, 1).getCellStyle().getDataFormatString());
    }

    @Test
    public void stylesAreMerged() {
        CellWriter cellWriter = createCellWriter();
        List<WritableCellStyle> styles = List.of(
                WritableCellStyles.format("abc"),
                WritableCellStyle.builder()
                        .format("a")
                        .backgroundColor(new StyleColor(1, 2, 3))
                        .build()
        );
        cellWriter.writeCell(new WritableCell("", 1, 1), styles);

        XSSFCellStyle cellStyle = ((XSSFCell) getCell(1, 1)).getCellStyle();
        assertEquals("abc", cellStyle.getDataFormatString());
        assertArrayEquals(new byte[]{1, 2, 3}, cellStyle.getFillForegroundColorColor().getRGB());
    }

    @Test
    public void cellRowOffsetIsApplied() {
        CellWriter cellWriter = createCellWriter();
        cellWriter.setOffSet(1, 0);

        CellLocation cellLocation = cellWriter.writeCell(new WritableCell("", 1, 1), List.of());

        assertEquals(2, cellLocation.row());
        assertEquals(1, cellLocation.column());
    }

    @Test
    public void cellColumnOffsetIsApplied() {
        CellWriter cellWriter = createCellWriter();
        cellWriter.setOffSet(0, 1);

        CellLocation cellLocation = cellWriter.writeCell(new WritableCell("", 1, 1), List.of());

        assertEquals(1, cellLocation.row());
        assertEquals(2, cellLocation.column());
    }

    @Test
    public void cellRowColumnOffsetIsApplied() {
        CellWriter cellWriter = createCellWriter();
        cellWriter.setOffSet(2, 2);

        CellLocation cellLocation = cellWriter.writeCell(new WritableCell("", 1, 1), List.of());

        assertEquals(3, cellLocation.row());
        assertEquals(3, cellLocation.column());
        assertNotNull(getCell(3, 3));
    }
}
