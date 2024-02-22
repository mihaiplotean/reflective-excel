package com.mihai.writer;

public class WritingContext {

    private final WritableSheet sheet;
//    private final SerializationContext serializationContext;
//    private final CellStyleContext cellStyleContext;

    public WritingContext(WritableSheet sheet) {
        this.sheet = sheet;
//        this.serializationContext = serializationContext;
//        this.cellStyleContext = cellStyleContext;
    }

//    public <T> Object getValueToWrite(Class<T> type, Object rowValue) {
//        return serializationContext.serialize((Class<Object>) type, rowValue);
//    }
//
//    public <T> Object getCellStyle(Class<T> type, Object rowValue) {
//        return serializationContext.serialize((Class<Object>) type, rowValue);
//    }
//
//    public WritableCellStyle getHeaderCellStyle(Object headerValue) {
//        return cellStyleContext.getHeaderStyle(this, headerValue);
//    }
}
