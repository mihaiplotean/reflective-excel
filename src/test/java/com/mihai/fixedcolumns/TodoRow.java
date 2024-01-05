package com.mihai.fixedcolumns;

import com.mihai.annotation.ExcelColumn;

import java.util.Date;
import java.util.Objects;

public class TodoRow {

    @ExcelColumn(name = "Description")  // todo: case insensitive!
    private String description;
    @ExcelColumn(name = "Deadline")
    private Date dueDate;
    @ExcelColumn(name = "Priority")
    private TodoPriority priority;

    public TodoRow() {
    }

    public String getDescription() {
        return description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public TodoPriority getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TodoRow todoRow = (TodoRow) o;
        return Objects.equals(description, todoRow.description)
                && Objects.equals(dueDate, todoRow.dueDate)
                && priority == todoRow.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, dueDate, priority);
    }
}
