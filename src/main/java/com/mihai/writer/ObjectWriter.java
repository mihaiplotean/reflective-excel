package com.mihai.writer;

import com.mihai.FieldAnalyzer;
import com.mihai.ReflectionUtilities;
import com.mihai.reader.field.CellValueField;
import com.mihai.reader.field.KeyValueField;
import com.mihai.reader.field.RowsField;
import com.mihai.writer.serializer.SerializationContext;
import com.mihai.writer.style.CellStyleContext;
import com.mihai.writer.style.WritableCellStyle;
import com.mihai.writer.table.WrittenTable;
import com.mihai.writer.table.WrittenTables;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class ObjectWriter {

    private final WritableSheet sheet;
    private final CellStyleContext cellStyleContext;
    private final SerializationContext serializationContext;
    private final ExcelWritingSettings settings;

    private final WrittenTables writtenTables;
    private final WritingContext context;

    public ObjectWriter(WritableSheet sheet, CellStyleContext cellStyleContext, SerializationContext serializationContext, ExcelWritingSettings settings) {
        this.sheet = sheet;
        this.cellStyleContext = cellStyleContext;
        this.serializationContext = serializationContext;
        this.settings = settings;
        this.writtenTables = new WrittenTables();
        this.context = new WritingContext(writtenTables);
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

        TableWriter tableWriter = new TableWriter(sheet, serializationContext, cellStyleContext, settings, context);
        for (RowsField rowsField : fieldAnalyzer.getExcelRowsFields()) {
            writeTable(tableWriter, rowsField, object);
        }
    }

    private void writeCellValues(CellWriter cellWriter, CellValueField valueField, Object object) {
        Field field = valueField.getField();
        Class<Object> type = (Class<Object>) field.getType();
        Object value = ReflectionUtilities.readField(field, object);
        WritableCellStyle style = cellStyleContext.getCellStyle(context, value);
        WritableCellStyle typeStyle = cellStyleContext.getTypeStyle(context, type);

        cellWriter.writeCell(
                new WritableCell(
                        value,
                        valueField.getRow(),
                        valueField.getColumn()
                ), List.of(style, typeStyle));
    }

    private void writePropertyValuePairs(CellWriter cellWriter, KeyValueField propertyField, Object object) {
        Field field = propertyField.getField();
        Class<Object> type = (Class<Object>) field.getType();
        Object value = ReflectionUtilities.readField(field, object);
        WritableCellStyle valueTypeStyle = cellStyleContext.getTypeStyle(context, type);
        WritableCellStyle valueCellStyle = cellStyleContext.getCellStyle(context, value);

        cellWriter.writeCell(
                new WritableCell(
                        value,
                        propertyField.getValueRow(),
                        propertyField.getValueColumn()
                ), List.of(valueCellStyle, valueTypeStyle));

        WritableCellStyle propertyTypeStyle = cellStyleContext.getTypeStyle(context, String.class);
        WritableCellStyle propertyCellStyle = cellStyleContext.getCellStyle(context, propertyField.getPropertyName());

        cellWriter.writeCell(
                new WritableCell(
                        propertyField.getPropertyName(),
                        propertyField.getPropertyRow(),
                        propertyField.getPropertyColumn()
                ), List.of(propertyCellStyle, propertyTypeStyle));
    }

    private void writeTable(TableWriter tableWriter, RowsField rowsField, Object object) {
        Field field = rowsField.getField();
        if (field.getType() == List.class) {
            List<Object> rows = (List<Object>) ReflectionUtilities.readField(field, object);
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<Object> argumentType = (Class<Object>) genericType.getActualTypeArguments()[0];  // todo: unsafe cast?

            WrittenTable table = tableWriter.writeTable(rows, argumentType, field.getName());

            writtenTables.append(table);
        } else {
            throw new IllegalStateException("Only lists allowed");
        }
    }
}
