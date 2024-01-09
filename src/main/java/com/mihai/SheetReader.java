package com.mihai;

import com.mihai.deserializer.DeserializationContext;
import com.mihai.field.AnnotatedField;
import com.mihai.field.value.AnnotatedFieldValue;
import com.mihai.workbook.sheet.PropertyCell;
import com.mihai.workbook.sheet.ReadableSheet;
import com.mihai.workbook.sheet.RowCells;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;

public class SheetReader implements RowReader {

    private final ReadableSheet sheet;
    private final ExcelReadingSettings settings;
    private final ReadingContext readingContext;

    private TableRowCellPointer rowCellPointer;
    private RowColumnDetector rowColumnDetector;
    private ColumnFieldMapping columnFieldMapping;

    public SheetReader(Sheet sheet, DeserializationContext deserializationContext, ExcelReadingSettings settings) {
        this.sheet = new ReadableSheet(sheet);
        this.settings = settings;
        this.rowCellPointer = new TableRowCellPointer(this.sheet);
        this.readingContext = new ReadingContext(rowCellPointer, deserializationContext, settings.getExceptionConsumer());
        this.rowColumnDetector = createRowColumnDetector();
    }

    private RowColumnDetector createRowColumnDetector() {
        return RowColumnDetector.builder(readingContext)
                .endRowDetector(settings.getEndRowDetector())
                .headerRowDetector(settings.getHeaderRowDetector())
                .skipColumnDetector(settings.getSkipColumnDetector())
                .skipRowDetector(settings.getSkipRowDetector())
                .build();
    }

    @Override
    public <T> List<T> readRows(Class<T> clazz) {
        List<T> rows = new ArrayList<>();

        RowCells headerRow = goToHeaderRow();
        if(headerRow == null) {
            return Collections.emptyList();
        }

        rowCellPointer.setCurrentTableHeaders(readHeaders());
        columnFieldMapping = createColumnFieldMapping(clazz);

        while (rowCellPointer.moreRowsExist()) {
            RowCells row = rowCellPointer.nextRow();

            // todo: row contains all cell, but should contain only the cells of the table?
            // todo: TableRow extends RowCells -> returned by rowCellPointer#getCurrentTableRow
            if (rowColumnDetector.isEndRow(row)) {
                break;
            }

            if (rowColumnDetector.shouldSkipRow(row)) {
                continue;
            }

            rows.add(createRow(clazz));
        }

        return List.copyOf(rows);
    }

    private RowCells goToHeaderRow() {
        while (rowCellPointer.moreRowsExist()) {
            RowCells row = rowCellPointer.nextRow();
            if (rowColumnDetector.isHeaderRow(row)) {
                return row;
            }
        }
        return null;
    }

    private TableHeaders readHeaders() {
        List<PropertyCell> headerCells = new ArrayList<>();
        RowCells row = rowCellPointer.getCurrentRow();

        for (PropertyCell cell : row) {
            if(StringUtils.isEmpty(cell.getValue())) {
                continue;
            }
            if (rowColumnDetector.shouldSkipColumn(cell)) {
                continue;
            }
            headerCells.add(cell);
        }

        int endRow = headerCells.stream()
                .map(PropertyCell::getBoundEndRow)
                .max(Integer::compare)
                .orElse(0);

        moveToRow(endRow);

        return new TableHeaderReader(sheet).read(rowCellPointer.getCurrentRow(), headerCells);
    }

    private void moveToRow(int rowNumber) {
        while (rowCellPointer.getCurrentRowNumber() < rowNumber) {
            rowCellPointer.nextRow();
        }
    }

    private ColumnFieldMapping createColumnFieldMapping(Class<?> clazz) {
        ColumnFieldMapping mapping = new ColumnFieldMapping(readingContext, clazz);
        mapping.create(readingContext.getCurrentTableHeaders());
        return mapping;
    }

    private <T> T createRow(Class<T> clazz) {
        T object = ReflectionUtilities.newObject(clazz);

        Map<AnnotatedField, AnnotatedFieldValue> fieldValues = new HashMap<>();

        while (rowCellPointer.moreCellsInRowExist()) {
            PropertyCell propertyCell = rowCellPointer.nextCell();

            AnnotatedField field = columnFieldMapping.getField(propertyCell.getColumnNumber());
            if (field != null) {
                AnnotatedFieldValue fieldValue = fieldValues.computeIfAbsent(field, AnnotatedField::newFieldValue);
                fieldValue.readValue(readingContext);
            }
        }

        fieldValues.values().forEach(fieldValue -> fieldValue.writeTo(object));

        return object;
    }

    public <T> T read(Class<T> clazz) {
        T object = ReflectionUtilities.newObject(clazz);

        FieldAnalyzer fieldAnalyzer = new FieldAnalyzer(clazz);

        List<AnnotatedField> fields = new ArrayList<>();
        fields.addAll(fieldAnalyzer.getExcelCellValueFields());
        fields.addAll(fieldAnalyzer.getExcelPropertyFields());
        fields.addAll(fieldAnalyzer.getExcelRowsFields(this));

        List<AnnotatedFieldValue> fieldValues = new ArrayList<>();
        fields.forEach(field -> {
            AnnotatedFieldValue fieldValue = field.newFieldValue();
            fieldValue.readValue(readingContext);
            fieldValues.add(fieldValue);
        });

        fieldValues.forEach(value -> value.writeTo(object));

        return object;
    }
}
