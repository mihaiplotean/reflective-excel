package com.reflectiveexcel.core.field;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.reflectiveexcel.core.annotation.DynamicColumns;
import com.reflectiveexcel.core.annotation.ExcelCellGroup;
import com.reflectiveexcel.core.annotation.ExcelCellValue;
import com.reflectiveexcel.core.annotation.ExcelColumn;
import com.reflectiveexcel.core.annotation.ExcelProperty;
import com.reflectiveexcel.core.annotation.TableId;
import com.reflectiveexcel.core.utils.ReflectionUtilities;
import com.reflectiveexcel.reader.detector.DynamicColumnDetector;
import org.apache.commons.lang3.reflect.FieldUtils;

public class FieldAnalyzer {

    private final Class<?> clazz;

    public FieldAnalyzer(Class<?> classToAnalyze) {
        this.clazz = classToAnalyze;
    }

    public List<FixedColumnField> getFixedColumnFields() {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(clazz, ExcelColumn.class);
        validateNonRepeatingColumnName(fields);
        return fields.stream()
                .map(field -> new FixedColumnField(field.getAnnotation(ExcelColumn.class).value(), field))
                .toList();
    }

    private static void validateNonRepeatingColumnName(List<Field> fields) {
        Set<String> usedColumnNames = new HashSet<>();
        for (Field field : fields) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            String columnName = annotation.value();
            if (usedColumnNames.contains(columnName.toLowerCase())) {
                throw new IllegalStateException(String.format(
                        "Duplicate column name \"%s\". Column names must be unique.", columnName
                ));
            }
            usedColumnNames.add(columnName.toLowerCase());
        }
    }

    public List<DynamicColumnField> getDynamicColumnFields() {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(clazz, DynamicColumns.class);
        return fields.stream()
                .map(field -> {
                    Class<? extends DynamicColumnDetector> detectorClazz = field.getAnnotation(DynamicColumns.class).detector();
                    DynamicColumnDetector detector = ReflectionUtilities.newObject(detectorClazz);
                    return new DynamicColumnField(field, detector);
                })
                .toList();
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

    public List<TableIdField> getTableIdFields() {
        return FieldUtils.getFieldsListWithAnnotation(clazz, TableId.class).stream()
                .map(field -> {
                    TableId annotation = field.getAnnotation(TableId.class);
                    return new TableIdField(field, annotation.value());
                })
                .toList();
    }

    public List<GroupedColumnsField> getCellGroupFields() {
        return FieldUtils.getFieldsListWithAnnotation(clazz, ExcelCellGroup.class).stream()
                .map(field -> new GroupedColumnsField(field.getAnnotation(ExcelCellGroup.class).value(), field))
                .toList();
    }
}
