package com.mihai.writer.style;

import com.mihai.writer.WritingContext;
import com.mihai.writer.serializer.CellSerializers;
import com.mihai.writer.serializer.WritableCellStyle;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DefaultStyleContext implements CellStyleContext {

    private final Map<Class<?>, StyleProvider> styleProviderMap = new HashMap<>();

    public DefaultStyleContext() {
        registerStyleProviders();
    }

    protected void registerStyleProviders() {
        registerStyleProvider(Date.class, StyleProviders.forDate());
    }

    @Override
    public <T> WritableCellStyle getStyle(WritingContext context, Class<T> clazz, T value) {
        StyleProvider styleProvider = styleProviderMap.get(clazz);
        if (styleProvider == null) {
            return null;
        }
        return styleProvider.getStyle(context, value);
    }

    @Override
    public void registerStyleProvider(Class<?> clazz, StyleProvider style) {
        styleProviderMap.put(clazz, style);
    }
}
