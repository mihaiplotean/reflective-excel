package com.mihai.writer.writers;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.mihai.core.field.CellValueField;
import com.mihai.core.field.FieldAnalyzer;
import com.mihai.core.field.KeyValueField;
import com.mihai.core.field.TableIdField;
import com.mihai.core.utils.ReflectionUtilities;
import com.mihai.writer.ExcelWritingSettings;
import com.mihai.writer.WritableCell;
import com.mihai.writer.WritableSheet;
import com.mihai.writer.WritableSheetContext;
import com.mihai.writer.style.WritableCellStyle;
import com.mihai.writer.table.WrittenTable;

public class ObjectWriter {

    private final WritableSheet sheet;
    private final WritableSheetContext sheetContext;
    private final ExcelWritingSettings settings;

    public ObjectWriter(WritableSheet sheet, WritableSheetContext sheetContext, ExcelWritingSettings settings) {
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
        for (TableIdField tableIdField : fieldAnalyzer.getTableIdFields()) {
            writeTable(tableWriter, tableIdField, object);
        }
    }

    @SuppressWarnings("unchecked")
    private void writeCellValues(CellWriter cellWriter, CellValueField valueField, Object object) {
        Field field = valueField.getField();
        Object value = ReflectionUtilities.readField(field, object);

        sheetContext.setCurrentRow(valueField.getRow());
        sheetContext.setCurrentColumn(valueField.getColumn());

        WritableCellStyle typeStyle = sheetContext.getTypeStyle((Class<Object>) field.getType(), value);
        WritableCellStyle cellStyle = sheetContext.getCellStyle(value);

        cellWriter.writeCell(
                new WritableCell(
                        value,
                        valueField.getRow(),
                        valueField.getColumn()
                ), List.of(typeStyle, cellStyle));
        sheetContext.resetCellPointer();
    }

    @SuppressWarnings("unchecked")
    private void writePropertyValuePairs(CellWriter cellWriter, KeyValueField propertyField, Object object) {
        Field field = propertyField.getField();
        Object value = ReflectionUtilities.readField(field, object);

        sheetContext.setCurrentRow(propertyField.getValueRow());
        sheetContext.setCurrentColumn(propertyField.getValueColumn());

        WritableCellStyle valueTypeStyle = sheetContext.getTypeStyle((Class<Object>) field.getType(), value);
        WritableCellStyle valueCellStyle = sheetContext.getCellStyle(value);

        cellWriter.writeCell(
                new WritableCell(
                        value,
                        propertyField.getValueRow(),
                        propertyField.getValueColumn()
                ), List.of(valueTypeStyle, valueCellStyle));

        sheetContext.setCurrentRow(propertyField.getPropertyRow());
        sheetContext.setCurrentColumn(propertyField.getPropertyColumn());

        String propertyName = propertyField.getPropertyName();
        WritableCellStyle propertyTypeStyle = sheetContext.getTypeStyle(String.class, propertyName);
        WritableCellStyle propertyCellStyle = sheetContext.getCellStyle(propertyName);

        cellWriter.writeCell(
                new WritableCell(
                        propertyName,
                        propertyField.getPropertyRow(),
                        propertyField.getPropertyColumn()
                ), List.of(propertyTypeStyle, propertyCellStyle));
        sheetContext.resetCellPointer();
    }

    @SuppressWarnings("unchecked")
    private void writeTable(TableWriter tableWriter, TableIdField tableIdField, Object object) {
        Field field = tableIdField.getField();
        if (field.getType() == List.class) {
            List<Object> rows = (List<Object>) ReflectionUtilities.readField(field, object);
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<Object> argumentType = (Class<Object>) genericType.getActualTypeArguments()[0];

            WrittenTable table = tableWriter.writeTable(rows, argumentType, tableIdField.getTableId());

            sheetContext.appendTable(table);
        } else {
            throw new IllegalStateException("Only lists can be annotated with @TableId");
        }
    }
}
