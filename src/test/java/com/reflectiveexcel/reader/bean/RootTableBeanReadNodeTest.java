package com.reflectiveexcel.reader.bean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Map;

import com.reflectiveexcel.core.annotation.DynamicColumns;
import com.reflectiveexcel.core.annotation.ExcelCellGroup;
import com.reflectiveexcel.core.annotation.ExcelColumn;
import org.junit.jupiter.api.Test;

public class RootTableBeanReadNodeTest {

    @Test
    public void beanTreeStructureCorrectlyCreated() {
        RootTableBeanReadNode beanNode = new RootTableBeanReadNode(TestBean.class);

        List<ChildBeanReadNode> childBeanNodes = beanNode.getChildren();
        assertEquals(3, childBeanNodes.size());
        assertEquals("fixed column", childBeanNodes.get(0).getName());
        assertNull(childBeanNodes.get(1).getName());
        ChildBeanReadNode groupBean = childBeanNodes.get(2);
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
