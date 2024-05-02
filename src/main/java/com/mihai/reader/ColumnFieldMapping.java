package com.mihai.reader;

import com.mihai.reader.bean.ChildBeanNode;
import com.mihai.reader.bean.RootTableBeanNode;
import com.mihai.reader.field.AnnotatedFieldType;
import com.mihai.reader.field.DynamicColumnField;
import com.mihai.reader.field.FixedColumnField;
import com.mihai.reader.field.GroupedColumnsField;
import com.mihai.reader.field.mapping.DynamicHeadersMappedField;
import com.mihai.reader.field.mapping.FixedHeaderMappedField;
import com.mihai.reader.field.mapping.GroupedHeadersMappedField;
import com.mihai.reader.field.mapping.HeaderMappedField;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mihai.reader.field.AnnotatedFieldType.*;

public class ColumnFieldMapping {

    private static final List<AnnotatedFieldType> FIELD_MAPPING_ORDER = List.of(
            FIXED,
            GROUPED,
            DYNAMIC
    );

    private final ReadingContext context;
    private final Map<Integer, HeaderMappedField> columnIndexToFieldMap = new HashMap<>();
//    private final FieldAnalyzer fieldAnalyzer;
    private final RootTableBeanNode tableBean;

    public ColumnFieldMapping(ReadingContext context, Class<?> clazz) {
        this.context = context;
//        this.fieldAnalyzer = new FieldAnalyzer(clazz);
        this.tableBean = new RootTableBeanNode(clazz);
    }

    public void create(TableHeaders headers) {
//        List<FixedColumnField2> fixedColumnFields = fieldAnalyzer.getFixedColumnFields();
//        List<DynamicColumnField> dynamicColumnFields = fieldAnalyzer.getDynamicColumnFields();
//        List<GroupedColumnsField> cellGroupFields = fieldAnalyzer.getCellGroupFields();
//        RootTableBeanNode tableBean = context.getCurrentTableBean();
        List<ChildBeanNode> childBeanNodes = tableBean.getLeaves().stream()
                .sorted(Comparator.comparing(node -> FIELD_MAPPING_ORDER.indexOf(node.getAnnotatedFieldType())))
                .toList();

        for (TableHeader header : headers) {
            HeaderMappedField matchingField = findMatchingField(childBeanNodes, header);
            columnIndexToFieldMap.put(header.getColumnNumber(), matchingField);
        }
    }

    private HeaderMappedField findMatchingField(List<ChildBeanNode> fields, TableHeader header) {
        return fields.stream()
                .map(this::getMappingField)
                .filter(field -> field.canMapTo(context, header))
                .findFirst()
                .orElse(null);
    }

    private HeaderMappedField getMappingField(ChildBeanNode node) {
        return switch (node.getAnnotatedFieldType()) {
            case FIXED -> new FixedHeaderMappedField((FixedColumnField) node);
            case DYNAMIC -> new DynamicHeadersMappedField((DynamicColumnField) node);
            case GROUPED -> new GroupedHeadersMappedField((GroupedColumnsField) node);
            default -> throw new IllegalArgumentException("Cannot map node %s to a column".formatted(node.getType()));
        };
    }

    public HeaderMappedField getField(int columnIndex) {
        return columnIndexToFieldMap.get(columnIndex);
    }

    public List<HeaderMappedField> getAllFields() {
        return List.copyOf(columnIndexToFieldMap.values());
    }
}
