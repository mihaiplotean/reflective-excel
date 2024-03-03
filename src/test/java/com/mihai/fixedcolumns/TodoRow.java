package com.mihai.fixedcolumns;

import com.mihai.annotation.ExcelColumn;

import java.util.Date;
import java.util.Objects;

public class TodoRow {

    @ExcelColumn(name = "Description")  // todo: case insensitive!
    private String description;
    @ExcelColumn(name = "Priority")
    private TodoPriority priority;
    @ExcelColumn(name = "Deadline")
    private Date dueDate;

    public TodoRow() {
    }

    public TodoRow(String description, Date dueDate, TodoPriority priority) {
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
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
