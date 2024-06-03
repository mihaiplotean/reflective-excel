package com.mihai.writer.node;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.util.List;

import com.mihai.core.annotation.DynamicColumns;
import org.junit.jupiter.api.Test;

class DynamicBeanWriteNodeTest {

    @Test
    public void annotatingListAsDynamicColumnThrowsException() throws NoSuchFieldException {
        Field field = DummyValue.class.getDeclaredField("values");
        assertThrows(IllegalStateException.class, () -> new DynamicBeanWriteNode(field, new DummyValue()));
    }

    public class DummyValue {

        @DynamicColumns
        private List<Object> values;
    }
}
