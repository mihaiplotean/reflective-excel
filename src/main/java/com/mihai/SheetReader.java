package com.mihai;

import com.mihai.deserializer.DeserializationContext;
import com.mihai.field.AnnotatedField;
import com.mihai.field.value.AnnotatedFieldValue;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SheetReader implements RowReader {

    private final ReadableSheet sheet;
    private final ExcelReadingSettings settings;

    private ReadingContext readingContext;
    private TableRowCellPointer rowCellPointer;
    private ColumnToFieldMapping columnFieldMapping;

    public SheetReader(Sheet sheet, DeserializationContext deserializationContext, ExcelReadingSettings settings) {
        this.sheet = new ReadableSheet(sheet);
        this.settings = settings;
        this.rowCellPointer = new TableRowCellPointer(this.sheet);
        this.readingContext = new ReadingContext(rowCellPointer, deserializationContext);
    }

    public <T> List<T> readRows(Class<T> clazz) {
        List<T> rows = new ArrayList<>();

        while (rowCellPointer.moreRowsExist()) {
            RowCells row = rowCellPointer.nextRow();

            if (row.getRowNumber() < settings.getHeaderStartRow()) {
                continue;
            }
            if (row.getRowNumber() == settings.getHeaderStartRow()) {
                rowCellPointer.setCurrentTableHeaders(readHeaders());
                columnFieldMapping = createColumnToFieldMapping(clazz);
                continue;
            }
            if (settings.isEndRow(row)) {
                break;
            }
            rows.add(createRow(clazz));
        }

        return List.copyOf(rows);
    }

    private TableHeaders readHeaders() {
        List<PropertyCell> headers = new ArrayList<>();
        for (PropertyCell cell : rowCellPointer.getCurrentRow()) {
            if (cell.getColumnNumber() >= settings.getHeaderStartColumn()) {
                headers.add(cell);
            }
        }
        return new TableHeaders(headers);
    }

    private ColumnToFieldMapping createColumnToFieldMapping(Class<?> clazz) {
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
            if(field != null) {
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
