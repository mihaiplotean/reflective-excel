package com.mihai;

import com.mihai.field.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnFieldMapping {

    private final ReadingContext context;
    private final Map<Integer, AnnotatedField> columnIndexToFieldMap = new HashMap<>();
    private final FieldAnalyzer fieldAnalyzer;

    public ColumnFieldMapping(ReadingContext context, Class<?> clazz) {
        this.context = context;
        this.fieldAnalyzer = new FieldAnalyzer(clazz);
    }

    public void create(TableHeaders headers) {
        List<FixedColumnField> fixedColumnFields = fieldAnalyzer.getFixedColumnFields();
        List<DynamicColumnField> dynamicColumnFields = fieldAnalyzer.getDynamicColumnFields();
        List<GroupedColumnsField> cellGroupFields = fieldAnalyzer.getCellGroupFields();

        for (TableHeader header : headers) {
            AnnotatedField matchingField = findMatchingField(cellGroupFields, header.getRoot());
            if(matchingField == null) {
                matchingField = findMatchingField(fixedColumnFields, header);
            }
            if(matchingField == null) {
                matchingField = findMatchingField(dynamicColumnFields, header);
            }
            columnIndexToFieldMap.put(header.getColumnNumber(), matchingField);
        }
    }

    private AnnotatedField findMatchingField(List<? extends AnnotatedHeaderField> fields, TableHeader header) {
        return fields.stream()
                .filter(field -> field.canMapTo(context, header))
                .findFirst()
                .orElse(null);
    }

    public AnnotatedField getField(int columnIndex) {
        return columnIndexToFieldMap.get(columnIndex);
    }
}
