package com.mihai.core.workbook;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class WorkbookFromInputStreamCreator implements WorkbookCreator {

    private final InputStream inputStream;

    public WorkbookFromInputStreamCreator(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public Workbook create() throws IOException {
        return WorkbookFactory.create(inputStream);
    }
}
