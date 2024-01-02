package com.mihai.workbook;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;

public interface WorkbookCreator {

    Workbook create() throws IOException;
}
