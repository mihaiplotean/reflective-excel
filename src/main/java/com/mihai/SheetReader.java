package com.mihai;

import com.mihai.deserializer.DeserializationContext;
import com.mihai.field.AnnotatedField;
import com.mihai.field.value.AnnotatedFieldValue;
import com.mihai.workbook.sheet.ReadableCell;
import com.mihai.workbook.sheet.ReadableSheet;
import com.mihai.workbook.sheet.ReadableRow;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;

public class SheetReader implements RowReader {

    private final ReadableSheet sheet;
    private final ExcelReadingSettings settings;
    private final ReadingContext readingContext;

    private TableRowCellPointer rowCellPointer;
    private ColumnFieldMapping columnFieldMapping;

    private RowColumnDetector rowColumnDetector;

    public SheetReader(Sheet sheet, DeserializationContext deserializationContext, ExcelReadingSettings settings) {
        this.sheet = new ReadableSheet(sheet);
        this.settings = settings;
        this.rowCellPointer = new TableRowCellPointer(this.sheet);
        this.readingContext = new ReadingContext(rowCellPointer, deserializationContext, settings.getExceptionConsumer());
    }

    private RowColumnDetector createRowColumnDetector() {
        return RowColumnDetector.builder(readingContext)
                .lastRowDetector(settings.getLastRowDetector())
                .headerRowDetector(settings.getHeaderRowDetector())
                .headerStartColumnDetector(settings.getHeaderStartColumnDetector())
                .headerLastColumnDetector(settings.getHeaderLastColumnDetector())
                .skipRowDetector(settings.getSkipRowDetector())
                .build();
    }

    @Override
    public ExcelReadingSettings getSettings() {
        return settings;
    }

    @Override
    public <T> List<T> readRows(Class<T> clazz) {
        return readRows(clazz, createRowColumnDetector());
    }

    @Override
    public <T> List<T> readRows(Class<T> clazz, RowColumnDetector rowColumnDetector) {
        this.rowColumnDetector = rowColumnDetector;

        List<T> rows = new ArrayList<>();

        rowCellPointer.reset();
        ReadableRow headerRow = goToHeaderRow();
        if(headerRow == null) {
            return Collections.emptyList();
        }

        rowCellPointer.setCurrentTableHeaders(readHeaders());
        columnFieldMapping = createColumnFieldMapping(clazz);

        while (rowCellPointer.moreRowsExist()) {
            ReadableRow row = rowCellPointer.nextRow();

            // todo: row contains all cell, but should contain only the cells of the table?
            // todo: TableRow extends RowCells -> returned by rowCellPointer#getCurrentTableRow
            if (rowColumnDetector.isLastRow(row)) {
                rows.add(createRow(clazz));
                break;
            }

            if (rowColumnDetector.shouldSkipRow(row)) {
                continue;
            }

            rows.add(createRow(clazz));
        }

        return rows;
    }

    private ReadableRow goToHeaderRow() {
        while (rowCellPointer.moreRowsExist()) {
            ReadableRow row = rowCellPointer.nextRow();
            if (rowColumnDetector.isHeaderRow(row)) {
                return row;
            }
        }
        return null;
    }

    private TableHeaders readHeaders() {
        List<ReadableCell> headerCells = new ArrayList<>();
        ReadableRow row = rowCellPointer.getCurrentRow();

        int startColumn = row.stream()
                .filter(cell -> rowColumnDetector.isHeaderStartColumn(cell))
                .findFirst()
                .map(ReadableCell::getColumnNumber)
                .orElse(Integer.MAX_VALUE);

        int endColumn = row.stream()
                .filter(cell -> cell.getColumnNumber() >= startColumn)
                .filter(cell -> rowColumnDetector.isHeaderLastColumn(cell))
                .findFirst()
                .map(ReadableCell::getColumnNumber)
                .orElse(Integer.MIN_VALUE);

        for (ReadableCell cell : row) {
            int columnNumber = cell.getColumnNumber();
            if(startColumn <= columnNumber && columnNumber <= endColumn) {
                headerCells.add(cell);
            }
        }

        int endRow = headerCells.stream()
                .map(ReadableCell::getBoundEndRow)
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
            ReadableCell cell = rowCellPointer.nextCell();

            AnnotatedField field = columnFieldMapping.getField(cell.getColumnNumber());
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
