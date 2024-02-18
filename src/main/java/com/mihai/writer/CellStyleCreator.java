package com.mihai.writer;

import com.mihai.writer.serializer.WritableCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Map;

public class CellStyleCreator {

    private final Workbook workbook;
    private final Map<String, Short> formatToIndexMap = new HashMap<>();
    private final CreationHelper creationHelper;

    public CellStyleCreator(Workbook workbook) {
        this.workbook = workbook;
        creationHelper = workbook.getCreationHelper();
    }

    public CellStyle getCellStyle(WritableCellStyle style) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(findOrCreateFormat(style.getFormat()));
        return cellStyle;
    }

    private short findOrCreateFormat(String format) {
        return formatToIndexMap.computeIfAbsent(format, this::createFormat);
    }

    private short createFormat(String format) {
        return creationHelper.createDataFormat().getFormat(format);
    }
}
