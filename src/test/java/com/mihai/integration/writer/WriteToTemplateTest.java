package com.mihai.integration.writer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

import com.mihai.assertion.ExcelAssert;
import com.mihai.core.annotation.ExcelColumn;
import com.mihai.core.workbook.CellLocation;
import com.mihai.writer.ExcelWritingSettings;
import com.mihai.writer.ReflectiveExcelWriter;
import org.junit.jupiter.api.Test;

public class WriteToTemplateTest {

    @Test
    public void writingUsingTemplateExpectedExcelFile() throws IOException, URISyntaxException {
        List<DummyRow> rows = List.of(
                new DummyRow("A", "B"),
                new DummyRow("C", "D")
        );

        File templateFile = new File(getClass().getResource("/for-template-test.xlsx").toURI());
        ExcelWritingSettings settings = ExcelWritingSettings.builder()
                .templateFile(templateFile)
                .tableStartCellLocator((context, tableId) -> CellLocation.fromReference("F7"))
                .build();
        File actualFile = File.createTempFile("reflective-excel-writer", "write-to-template.xlsx");
        new ReflectiveExcelWriter(actualFile, settings).writeRows(rows, DummyRow.class);

        try (InputStream expectedInputStream = getClass().getResourceAsStream("/test-write-to-template.xlsx")) {
            ExcelAssert.assertThat(actualFile)
                    .isEqualTo(expectedInputStream);
        }
    }

    public static class DummyRow {

        @ExcelColumn("Column A")
        private final String valueA;

        @ExcelColumn("Column B")
        private final String valueB;

        public DummyRow(String valueA, String valueB) {
            this.valueA = valueA;
            this.valueB = valueB;
        }
    }
}
