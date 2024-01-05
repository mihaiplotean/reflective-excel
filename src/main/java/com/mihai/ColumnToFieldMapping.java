package com.mihai;

import com.mihai.detector.DynamicColumnDetector;
import com.mihai.field.AnnotatedField;
import com.mihai.field.DynamicColumnField;
import com.mihai.field.FixedColumnField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnToFieldMapping {

    private final Map<Integer, AnnotatedField> columnIndexToFieldMap = new HashMap<>();
    private final FieldAnalyzer fieldAnalyzer;

    public ColumnToFieldMapping(Class<?> clazz) {
        this.fieldAnalyzer = new FieldAnalyzer(clazz);
    }

    public void create(TableRowCellPointer rowCellPointer, ReadingContext context) {  // todo: make these class fields?
        List<FixedColumnField> fixedColumnFields = fieldAnalyzer.getFixedColumnFields();
        List<DynamicColumnField> dynamicColumnFields = fieldAnalyzer.getDynamicColumnFields();

        while (rowCellPointer.moreCellsInRowExist()) {
            PropertyCell headerCell = rowCellPointer.nextCell();

            AnnotatedField matchingField = fixedColumnFields.stream()
                    .filter(field -> field.getColumnName().equalsIgnoreCase(headerCell.getValue()))
                    .findFirst()
                    .map(field -> (AnnotatedField) field)
                    .orElseGet(() -> {
                        for (DynamicColumnField dynamicColumnField : dynamicColumnFields) {
                            DynamicColumnDetector detector = dynamicColumnField.getColumnDetector();
                            if (detector.isDynamic(context, headerCell)) {
                                return dynamicColumnField;
                            }
                        }
                        return null;
                    });
            columnIndexToFieldMap.put(headerCell.getColumnNumber(), matchingField);
        }
    }

    public AnnotatedField getField(int columnIndex) {
        return columnIndexToFieldMap.get(columnIndex);
    }
}
