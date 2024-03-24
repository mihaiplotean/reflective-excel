package com.mihai.integration.multipletables.destination;

import com.mihai.annotation.ExcelColumn;

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
}
