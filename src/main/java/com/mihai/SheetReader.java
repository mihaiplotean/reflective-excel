package com.mihai;

import com.mihai.deserializer.DeserializationContext;
import com.mihai.exception.BadInputException;
import com.mihai.field.AnnotatedField;
import com.mihai.field.value.AnnotatedFieldValue;
import com.mihai.workbook.PropertyCell;
import com.mihai.workbook.ReadableSheet;
import com.mihai.workbook.RowCells;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;

public class SheetReader implements RowReader {

    private final ReadableSheet sheet;
    private final ExcelReadingSettings settings;

    private ReadingContext readingContext;
    private TableRowCellPointer rowCellPointer;
    private RowColumnDetector rowColumnDetector;
    private ColumnToFieldMapping columnFieldMapping;

    public SheetReader(Sheet sheet, DeserializationContext deserializationContext, ExcelReadingSettings settings) {
        this.sheet = new ReadableSheet(sheet);
        this.settings = settings;
        this.rowCellPointer = new TableRowCellPointer(this.sheet);
        this.readingContext = new ReadingContext(rowCellPointer, deserializationContext);
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
        List<PropertyCell> headers = new ArrayList<>();
        RowCells row = rowCellPointer.getCurrentRow();

        for (PropertyCell cell : row) {
            if (rowColumnDetector.shouldSkipColumn(cell)) {
                continue;
            }
            headers.add(cell);
        }
        return new TableHeaders(row.getRowNumber(), headers);
    }

    private ColumnToFieldMapping createColumnFieldMapping(Class<?> clazz) {
        ColumnToFieldMapping mapping = new ColumnToFieldMapping(clazz);
        mapping.create(rowCellPointer, readingContext);
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
                readFieldValue(fieldValue);
            }
        }

        fieldValues.values().forEach(fieldValue -> fieldValue.writeTo(object));

        return object;
    }

    private void readFieldValue(AnnotatedFieldValue value) {
        try {
            value.readValue(readingContext);
        }
        catch (BadInputException e) {
            settings.getExceptionConsumer().accept(readingContext.getCurrentRow(), e);
        }
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
            readFieldValue(fieldValue);
            fieldValues.add(fieldValue);
        });

        fieldValues.forEach(value -> value.writeTo(object));

        return object;
    }
}
