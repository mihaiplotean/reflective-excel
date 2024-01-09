package com.mihai;

import com.mihai.annotation.*;
import com.mihai.detector.ColumnDetector;
import com.mihai.field.*;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.*;

public class FieldAnalyzer {

    private static final Set<Class<?>> SUPPORTED_DYNAMIC_FIELD_TYPES = Set.of(
            List.class,
            Map.class
    );

    private final Class<?> clazz;

    public FieldAnalyzer(Class<?> classToAnalyze) {
        this.clazz = classToAnalyze;
    }

    public List<FixedColumnField> getFixedColumnFields() {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(clazz, ExcelColumn.class);
        validateNonRepeatingColumnName(fields);
        return fields.stream()
                .map(field -> new FixedColumnField(field.getAnnotation(ExcelColumn.class).name(), field))
                .toList();
    }

    private void validateNonRepeatingColumnName(List<Field> fields) {
        Set<String> usedColumnNames = new HashSet<>();
        for (Field field : fields) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            String columnName = annotation.name();
            if(usedColumnNames.contains(columnName)) {
                throw new IllegalStateException(String.format(
                        "Duplicate column name \"%s\". Column names must be unique.", columnName
                ));
            }
            usedColumnNames.add(columnName);
        }
    }

    public List<DynamicColumnField> getDynamicColumnFields() {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(clazz, DynamicColumns.class);
        fields.forEach(this::validateDynamicFieldType);
//        fields.forEach();  // validate empty constructor existence?
        return fields.stream()
                .map(field -> {
                    Class<? extends ColumnDetector> detectorClazz = field.getAnnotation(DynamicColumns.class).detector();
                    ColumnDetector detector = ReflectionUtilities.newObject(detectorClazz);
                    return new DynamicColumnField(detector, field);
                })
                .toList();
    }

    private void validateDynamicFieldType(Field field) {
        Class<?> type = field.getType();  // todo: move these validation checks to the constructors of the annotated-fields
        if (isSupportedDynamicField(type)) {
            return;
        }
        throw new IllegalStateException(String.format(
                "Unsupported type %s annotated as dynamic column. Only <%s> can be annotated.", type, SUPPORTED_DYNAMIC_FIELD_TYPES
        ));
    }

    private boolean isSupportedDynamicField(Class<?> clazz) {
        return SUPPORTED_DYNAMIC_FIELD_TYPES.stream()
                .anyMatch(clazz::equals);
    }

    private void validateDynamicFieldHasEmptyConstructor(Field field) {
        Class<? extends ColumnDetector> detector = field.getAnnotation(DynamicColumns.class).detector();
    }

    public List<CellValueField> getExcelCellValueFields() {
        return FieldUtils.getFieldsListWithAnnotation(clazz, ExcelCellValue.class).stream()
                .map(field -> new CellValueField(field.getAnnotation(ExcelCellValue.class).cellReference(), field))
                .toList();
    }

    public List<KeyValueField> getExcelPropertyFields() {
        return FieldUtils.getFieldsListWithAnnotation(clazz, ExcelProperty.class).stream()
                .map(field -> {
                    ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                    return new KeyValueField(annotation.name(), annotation.nameReference(), annotation.valueReference(), field);
                })
                .toList();
    }

    public List<RowsField> getExcelRowsFields(RowReader rowReader) {
        return FieldUtils.getFieldsListWithAnnotation(clazz, ExcelRows.class).stream()
                .map(field -> new RowsField(rowReader, field))
                .toList();
    }

    public List<GroupedColumnsField> getCellGroupFields() {
        return FieldUtils.getFieldsListWithAnnotation(clazz, ExcelCellGroup.class).stream()
                .map(field -> new GroupedColumnsField(field.getAnnotation(ExcelCellGroup.class).name(), field))
                .toList();
    }
}
