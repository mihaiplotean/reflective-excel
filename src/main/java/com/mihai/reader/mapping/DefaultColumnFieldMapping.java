package com.mihai.reader.mapping;

import com.mihai.core.field.*;
import com.mihai.reader.ReadingContext;
import com.mihai.reader.bean.ChildBeanReadNode;
import com.mihai.reader.bean.RootTableBeanReadNode;
import com.mihai.reader.table.TableHeader;
import com.mihai.reader.table.TableHeaders;

import java.util.*;

import static com.mihai.core.field.AnnotatedFieldType.*;

public class DefaultColumnFieldMapping implements ColumnFieldMapping {

    private static final List<AnnotatedFieldType> FIELD_MAPPING_ORDER = List.of(
            FIXED,
            GROUPED,
            DYNAMIC
    );

    private final ReadingContext context;
    private final RootTableBeanReadNode tableBean;

    private final Map<AnnotatedField, HeaderMappedField> annotatedToMappingField = new HashMap<>();
    private final Map<Integer, HeaderMappedField> columnIndexToFieldMap = new HashMap<>();

    public DefaultColumnFieldMapping(ReadingContext context, Class<?> clazz) {
        this.context = context;
        this.tableBean = new RootTableBeanReadNode(clazz);
    }

    @Override
    public void create(TableHeaders headers) {
        List<ChildBeanReadNode> childBeanNodes = tableBean.getChildren().stream()
                .sorted(Comparator.comparing(node -> FIELD_MAPPING_ORDER.indexOf(node.getAnnotatedFieldType())))
                .toList();

        for (TableHeader header : headers) {
            HeaderMappedField matchingField = findMatchingField(childBeanNodes, header);
            columnIndexToFieldMap.put(header.getColumnNumber(), matchingField);
        }
    }

    private HeaderMappedField findMatchingField(List<ChildBeanReadNode> fields, TableHeader header) {
        return fields.stream()
                .map(ChildBeanReadNode::getAnnotatedField)
                .map(this::getOrCreateMappingField)
                .filter(field -> field.canMapTo(context, header))
                .findFirst()
                .orElse(null);
    }

    private HeaderMappedField getOrCreateMappingField(AnnotatedField annotatedField) {
        return annotatedToMappingField.computeIfAbsent(annotatedField, this::createMappingField);
    }

    private HeaderMappedField createMappingField(AnnotatedField annotatedField) {
        return switch (annotatedField.getType()) {
            case FIXED -> new FixedHeaderMappedField((FixedColumnField) annotatedField);
            case DYNAMIC -> new DynamicHeadersMappedField((DynamicColumnField) annotatedField);
            case GROUPED -> new GroupedHeadersMappedField((GroupedColumnsField) annotatedField);
            default -> throw new IllegalArgumentException("Cannot map node %s to a column".formatted(annotatedField.getType()));
        };
    }

    @Override
    public HeaderMappedField getField(int columnIndex) {
        return columnIndexToFieldMap.get(columnIndex);
    }

    @Override
    public List<HeaderMappedField> getAllFields() {
        return columnIndexToFieldMap.values().stream()
                .filter(Objects::nonNull)
                .toList();
    }
}
