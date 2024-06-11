package com.mihai.reader;

import com.mihai.reader.deserializer.CellDeserializer;
import com.mihai.reader.deserializer.DefaultDeserializationContext;
import com.mihai.reader.deserializer.DeserializationContext;
import com.mihai.reader.detector.AutoRowColumnDetector;
import com.mihai.reader.detector.SimpleRowColumnDetector;
import com.mihai.reader.detector.TableRowColumnDetector;
import com.mihai.reader.exception.BadInputExceptionConsumer;

/**
 * Allows to customize the sheet reading process.
 */
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

        /**
         * Specifies the name of the sheet to be read.
         * The matching is case-insensitive.
         */
        public ExcelReadingSettingsBuilder sheetName(String sheetName) {
            this.sheetName = sheetName;
            return this;
        }

        /**
         * Specifies the index of the sheet to be read. By default, the first sheet is read.
         * Negative indices are also allowed to start from the last sheet, i.e. passing -1 will read the last sheet.
         * If a sheet name has also been specified using {@link #sheetName(String)}, the index is ignored.
         */
        public ExcelReadingSettingsBuilder sheetIndex(int sheetIndex) {
            this.sheetIndex = sheetIndex;
            return this;
        }

        /**
         * Specifies where does the table (header) start. By default, it is considered to be cell "A1".
         */
        public ExcelReadingSettingsBuilder headerStartCellReference(String cellReference) {
            return rowColumnDetector(new SimpleRowColumnDetector(cellReference));
        }

        /**
         * Specifies that the table start should be found automatically by the framework.
         */
        public ExcelReadingSettingsBuilder autoHeaderStartDetection() {
            return rowColumnDetector(new AutoRowColumnDetector());
        }

        public ExcelReadingSettingsBuilder rowColumnDetector(TableRowColumnDetector rowColumnDetector) {
            this.rowColumnDetector = rowColumnDetector;
            return this;
        }

        /**
         * Allows to intercept and handle a {@link com.mihai.reader.exception.BadInputException} that has been thrown
         * during deserialization.
         * By default, the exception is thrown further.
         */
        public ExcelReadingSettingsBuilder onException(BadInputExceptionConsumer exceptionConsumer) {
            this.exceptionConsumer = exceptionConsumer;
            return this;
        }

        /**
         * Allows to provide a custom deserialization context, which specifies how a cell value is converted to
         * a specific type.
         *
         * @param deserializationContext deserialization context to be used during reading.
         */
        public ExcelReadingSettingsBuilder setDeserializationContext(DeserializationContext deserializationContext) {
            this.deserializationContext = deserializationContext;
            return this;
        }

        /**
         * Allows adding a deserializer for the given class to the currently set deserialization context.
         *
         * @param clazz type for which the deserializer is registered.
         * @param deserializer the registered deserializer.
         */
        public <T> ExcelReadingSettingsBuilder registerDeserializer(Class<T> clazz, CellDeserializer<T> deserializer) {
            deserializationContext.registerDeserializer(clazz, deserializer);
            return this;
        }

        public ExcelReadingSettings build() {
            return new ExcelReadingSettings(this);
        }
    }
}
