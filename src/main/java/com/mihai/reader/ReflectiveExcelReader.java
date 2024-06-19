package com.mihai.reader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import com.mihai.core.ReflectiveExcelException;
import com.mihai.core.workbook.WorkbookCreator;
import com.mihai.core.workbook.WorkbookFromFileCreator;
import com.mihai.core.workbook.WorkbookFromInputStreamCreator;
import com.mihai.reader.readers.SheetReader;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Main class for reading tables and objects from the sheet.
 * Both .xlsx and .xls formats are supported.
 */
public class ReflectiveExcelReader {

    private final WorkbookCreator workbookCreator;
    private final ExcelReadingSettings settings;

    /**
     * Constructs an Excel file reader using the default settings.
     *
     * @param file Excel file.
     */
    public ReflectiveExcelReader(File file) {
        this(file, ExcelReadingSettings.DEFAULT);
    }

    /**
     * Constructs an Excel input stream reader using the default settings.
     *
     * @param inputStream input stream representing an Excel file.
     */
    public ReflectiveExcelReader(InputStream inputStream) {
        this(inputStream, ExcelReadingSettings.DEFAULT);
    }

    /**
     * Constructs an Excel file reader using the provided settings.
     *
     * @param file Excel file.
     * @param settings reading settings.
     */
    public ReflectiveExcelReader(File file, ExcelReadingSettings settings) {
        this(new WorkbookFromFileCreator(file), settings);
    }

    /**
     * Constructs an Excel input stream reader using the provided settings.
     *
     * @param inputStream input stream representing an Excel file.
     * @param settings reading settings.
     */
    public ReflectiveExcelReader(InputStream inputStream, ExcelReadingSettings settings) {
        this(new WorkbookFromInputStreamCreator(inputStream), settings);
    }

    private ReflectiveExcelReader(WorkbookCreator workbookCreator, ExcelReadingSettings settings) {
        this.workbookCreator = workbookCreator;
        this.settings = settings;
    }

    /**
     * Reads and returns the rows of the table corresponding to the provided type.
     *
     * @param clazz the type of the rows to be read.
     * @return the rows of the table.
     */
    public <T> List<T> readRows(Class<T> clazz) {
        try (Workbook workbook = workbookCreator.create()) {
            if (workbook.getNumberOfSheets() == 0) {
                return Collections.emptyList();
            }
            Sheet sheet = getSheet(workbook, settings);
            return new SheetReader(sheet, settings).readRows(clazz);
        } catch (IOException e) {
            throw new ReflectiveExcelException("Could not read excel file!", e);
        }
    }

    private Sheet getSheet(Workbook workbook, ExcelReadingSettings settings) {
        String sheetName = settings.getSheetName();
        if (sheetName == null) {
            int numberOfSheets = workbook.getNumberOfSheets();
            int sheetIndex = settings.getSheetIndex();
            return workbook.getSheetAt(translateIndex(sheetIndex, numberOfSheets));
        }
        return workbook.getSheet(sheetName);
    }

    private int translateIndex(int index, int total) {
        if (index >= 0) {
            return index;
        }
        return total + index;
    }

    /**
     * Reads and returns the object corresponding to the provided type. This can be seen as an object representing the
     * sheet to be read.
     * Compared to {@link #readRows(Class)}, this allows reading multiple tables at once, as well as storing
     * values corresponding to given cells.
     *
     * @param clazz the type of the object to be read.
     * @return the rows of the table.
     * @see com.mihai.core.annotation.TableId
     * @see com.mihai.core.annotation.ExcelCellValue
     * @see com.mihai.core.annotation.ExcelProperty
     */
    public <T> T read(Class<T> clazz) {
        try (Workbook workbook = workbookCreator.create()) {
            Sheet sheet = getSheet(workbook, settings);
            return new SheetReader(sheet, settings).read(clazz);
        } catch (IOException e) {
            throw new ReflectiveExcelException("Could not read excel file!", e);
        }
    }
}
