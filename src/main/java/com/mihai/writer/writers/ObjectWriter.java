package com.mihai.writer.writers;

import com.mihai.FieldAnalyzer;
import com.mihai.ReflectionUtilities;
import com.mihai.reader.field.CellValueField;
import com.mihai.reader.field.KeyValueField;
import com.mihai.reader.field.RowsField;
import com.mihai.writer.ExcelWritingSettings;
import com.mihai.writer.SheetContext;
import com.mihai.writer.WritableCell;
import com.mihai.writer.WritableSheet;
import com.mihai.writer.style.WritableCellStyle;
import com.mihai.writer.table.WrittenTable;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class ObjectWriter {

    private final WritableSheet sheet;
    private final SheetContext sheetContext;
    private final ExcelWritingSettings settings;

    public ObjectWriter(WritableSheet sheet, SheetContext sheetContext, ExcelWritingSettings settings) {
        this.sheet = sheet;
        this.sheetContext = sheetContext;
        this.settings = settings;
    }

    public void write(Object object) {
        FieldAnalyzer fieldAnalyzer = new FieldAnalyzer(object.getClass());

        CellWriter cellWriter = new CellWriter(sheet);
        for (CellValueField valueField : fieldAnalyzer.getExcelCellValueFields()) {
            writeCellValues(cellWriter, valueField, object);
        }

        for (KeyValueField propertyField : fieldAnalyzer.getExcelPropertyFields()) {
            writePropertyValuePairs(cellWriter, propertyField, object);
        }

        TableWriter tableWriter = new TableWriter(sheet, sheetContext, settings);
        for (RowsField rowsField : fieldAnalyzer.getExcelRowsFields()) {
            writeTable(tableWriter, rowsField, object);
        }
    }

    @SuppressWarnings("unchecked")
    private void writeCellValues(CellWriter cellWriter, CellValueField valueField, Object object) {
        Field field = valueField.getField();
        Class<Object> type = (Class<Object>) field.getType();
        Object value = ReflectionUtilities.readField(field, object);

        sheetContext.setCurrentRow(valueField.getRow());
        sheetContext.setCurrentColumn(valueField.getColumn());

        WritableCellStyle style = sheetContext.getCellStyle(value);
        WritableCellStyle typeStyle = sheetContext.getTypeStyle(type);

        cellWriter.writeCell(
                new WritableCell(
                        value,
                        valueField.getRow(),
                        valueField.getColumn()
                ), List.of(style, typeStyle));
        sheetContext.resetCellPointer();
    }

    @SuppressWarnings("unchecked")
    private void writePropertyValuePairs(CellWriter cellWriter, KeyValueField propertyField, Object object) {
        Field field = propertyField.getField();
        Class<Object> type = (Class<Object>) field.getType();
        Object value = ReflectionUtilities.readField(field, object);

        sheetContext.setCurrentRow(propertyField.getValueRow());
        sheetContext.setCurrentColumn(propertyField.getValueColumn());

        WritableCellStyle valueTypeStyle = sheetContext.getTypeStyle(type);
        WritableCellStyle valueCellStyle = sheetContext.getCellStyle(value);

        cellWriter.writeCell(
                new WritableCell(
                        value,
                        propertyField.getValueRow(),
                        propertyField.getValueColumn()
                ), List.of(valueCellStyle, valueTypeStyle));

        sheetContext.setCurrentRow(propertyField.getPropertyRow());
        sheetContext.setCurrentColumn(propertyField.getPropertyColumn());

        WritableCellStyle propertyTypeStyle = sheetContext.getTypeStyle(String.class);
        WritableCellStyle propertyCellStyle = sheetContext.getCellStyle(propertyField.getPropertyName());

        cellWriter.writeCell(
                new WritableCell(
                        propertyField.getPropertyName(),
                        propertyField.getPropertyRow(),
                        propertyField.getPropertyColumn()
                ), List.of(propertyCellStyle, propertyTypeStyle));
        sheetContext.resetCellPointer();
    }

    @SuppressWarnings("unchecked")
    private void writeTable(TableWriter tableWriter, RowsField rowsField, Object object) {
        Field field = rowsField.getField();
        if (field.getType() == List.class) {
            List<Object> rows = (List<Object>) ReflectionUtilities.readField(field, object);
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<Object> argumentType = (Class<Object>) genericType.getActualTypeArguments()[0];  // todo: unsafe cast?

            WrittenTable table = tableWriter.writeTable(rows, argumentType, field.getName());

            sheetContext.appendTable(table);
        } else {
            throw new IllegalStateException("Only lists allowed");
        }
    }
}
