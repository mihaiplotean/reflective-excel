package com.mihai.workbook;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;

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
