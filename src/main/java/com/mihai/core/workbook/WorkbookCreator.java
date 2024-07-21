package com.mihai.core.workbook;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;

public interface WorkbookCreator {

    Workbook create() throws IOException;
}
