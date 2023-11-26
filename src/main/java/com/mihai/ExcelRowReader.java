package com.mihai;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ExcelRowReader {

//    private static final DataFormatter DATA_FORMATTER = new DataFormatter();
//
//    public <T> List<T> readRows(Class<T> clazz) throws IOException, InvalidFormatException {
//        try (Workbook workbook = new XSSFWorkbook(file)) {
//            Sheet sheet = workbook.getSheetAt(0);
//            for (Row row : sheet) {
//                for (Cell cell : row) {
//                    CellType cellType = cell.getCellType();
//                    cell.getStringCellValue()
//                }
//            }
//        }
//
//        ExcelRow rowDetails = clazz.getAnnotation(ExcelRow.class);
//        return Collections.emptyList();
//    }
//
//    private static String getCellValueAsString(Cell cell) {
//        return DATA_FORMATTER.formatCellValue(cell);
//    }
}
