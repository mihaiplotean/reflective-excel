package com.mihai.integration.multipletables;

import com.mihai.annotation.ExcelColumn;

import java.util.Objects;

public class DestinationRow {

    @ExcelColumn(name = "Destination")
    private String destination;

    @ExcelColumn(name = "required")
    private int requiredNum;

    @ExcelColumn(name = "delivered")
    private int deliveredNum;

    public DestinationRow() {
    }

    public DestinationRow(String destination, int requiredNum, int deliveredNum) {
        this.destination = destination;
        this.requiredNum = requiredNum;
        this.deliveredNum = deliveredNum;
    }

    public String getDestination() {
        return destination;
    }

    public int getRequiredNum() {
        return requiredNum;
    }

    public int getDeliveredNum() {
        return deliveredNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DestinationRow that = (DestinationRow) o;
        return requiredNum == that.requiredNum
                && deliveredNum == that.deliveredNum
                && Objects.equals(destination, that.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destination, requiredNum, deliveredNum);
    }
}
