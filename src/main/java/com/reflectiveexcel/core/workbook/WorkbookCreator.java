package com.reflectiveexcel.core.workbook;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;

public interface WorkbookCreator {

    Workbook create() throws IOException;
}
