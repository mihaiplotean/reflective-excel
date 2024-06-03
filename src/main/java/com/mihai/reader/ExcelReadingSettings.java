package com.mihai.reader;

import com.mihai.reader.deserializer.CellDeserializer;
import com.mihai.reader.deserializer.DefaultDeserializationContext;
import com.mihai.reader.deserializer.DeserializationContext;
import com.mihai.reader.detector.AutoRowColumnDetector;
import com.mihai.reader.detector.SimpleRowColumnDetector;
import com.mihai.reader.detector.TableRowColumnDetector;
import com.mihai.reader.exception.BadInputExceptionConsumer;

public class ExcelReadingSettings {

    public static final ExcelReadingSettings DEFAULT = new ExcelReadingSettingsBuilder().build();

    private final String sheetName;
    private final int sheetIndex;

    private final TableRowColumnDetector rowColumnDetector;
    private final BadInputExceptionConsumer exceptionConsumer;
    private final DeserializationContext deserializationContext;

    private ExcelReadingSettings(ExcelReadingSettingsBuilder builder) {
        sheetName = builder.sheetName;
        sheetIndex = builder.sheetIndex;

        rowColumnDetector = builder.rowColumnDetector;
        exceptionConsumer = builder.exceptionConsumer;
        deserializationContext = builder.deserializationContext;
    }

    public String getSheetName() {
        return sheetName;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public TableRowColumnDetector getRowColumnDetector() {
        return rowColumnDetector;
    }

    public BadInputExceptionConsumer getExceptionConsumer() {
        return exceptionConsumer;
    }

    public DeserializationContext getDeserializationContext() {
        return deserializationContext;
    }

    public static ExcelReadingSettingsBuilder builder() {
        return new ExcelReadingSettingsBuilder();
    }

    public static final class ExcelReadingSettingsBuilder {

        private String sheetName;
        private int sheetIndex;

        private TableRowColumnDetector rowColumnDetector = new SimpleRowColumnDetector("A1");

        private BadInputExceptionConsumer exceptionConsumer = (row, exception) -> {
            throw exception;
        };

        private DeserializationContext deserializationContext = new DefaultDeserializationContext();

        public ExcelReadingSettingsBuilder sheetName(String sheetName) {
            this.sheetName = sheetName;
            return this;
        }

        public ExcelReadingSettingsBuilder sheetIndex(int sheetIndex) {
            this.sheetIndex = sheetIndex;
            return this;
        }

        public ExcelReadingSettingsBuilder headerStartCellReference(String cellReference) {
            return rowColumnDetector(new SimpleRowColumnDetector(cellReference));
        }

        public ExcelReadingSettingsBuilder autoRowColumnDetector() {
            return rowColumnDetector(new AutoRowColumnDetector());
        }

        public ExcelReadingSettingsBuilder rowColumnDetector(TableRowColumnDetector rowColumnDetector) {
            this.rowColumnDetector = rowColumnDetector;
            return this;
        }

        public ExcelReadingSettingsBuilder onException(BadInputExceptionConsumer exceptionConsumer) {
            this.exceptionConsumer = exceptionConsumer;
            return this;
        }

        public ExcelReadingSettingsBuilder setDeserializationContext(DeserializationContext deserializationContext) {
            this.deserializationContext = deserializationContext;
            return this;
        }

        public <T> ExcelReadingSettingsBuilder registerDeserializer(Class<T> clazz, CellDeserializer<T> deserializer) {
            deserializationContext.registerDeserializer(clazz, deserializer);
            return this;
        }

        public ExcelReadingSettings build() {
            return new ExcelReadingSettings(this);
        }
    }
}
