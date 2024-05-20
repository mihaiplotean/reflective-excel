package com.mihai.reader.bean;

import com.mihai.common.annotation.DynamicColumns;
import com.mihai.common.annotation.ExcelCellGroup;
import com.mihai.common.annotation.ExcelColumn;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RootTableBeanNodeTest {

    @Test
    public void beanTreeStructureCorrectlyCreated() {
        RootTableBeanNode beanNode = new RootTableBeanNode(TestBean.class);

        List<ChildBeanNode> childBeanNodes = beanNode.getChildren();
        assertEquals(3, childBeanNodes.size());
        assertEquals("fixed column", childBeanNodes.get(0).getName());
        assertNull(childBeanNodes.get(1).getName());
        ChildBeanNode groupBean = childBeanNodes.get(2);
        assertEquals("group A", groupBean.getName());
        assertEquals(1, groupBean.getChildren().size());
        assertEquals("sub column", groupBean.getChildren().get(0).getName());
    }

    private static class TestBean {

        @ExcelColumn("fixed column")
        private Integer fixedColumn;

        @DynamicColumns
        private Map<String, String> dynamicColumns;

        @ExcelCellGroup("group A")
        private TestSubBean groupedColumns;

        private static class TestSubBean {

            @ExcelColumn("sub column")
            private Integer fixedColumn;
        }
    }
}
