package com.mihai.writer;

import com.mihai.writer.serializer.SerializationContext;

public class WritingContext {

    private final WritableSheet sheet;
    private final SerializationContext serializationContext;

    public WritingContext(WritableSheet sheet, SerializationContext serializationContext) {
        this.sheet = sheet;
        this.serializationContext = serializationContext;
    }


}
