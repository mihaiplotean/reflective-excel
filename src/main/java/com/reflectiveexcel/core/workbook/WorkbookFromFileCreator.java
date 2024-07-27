package com.reflectiveexcel.core.workbook;

import java.io.File;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class WorkbookFromFileCreator implements WorkbookCreator {

    public final File file;

    public WorkbookFromFileCreator(File file) {
        this.file = file;
    }

    @Override
    public Workbook create() throws IOException {
        return WorkbookFactory.create(file);
    }
}
