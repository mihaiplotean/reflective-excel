package com.mihai.integration.groupedcolumns;

import com.mihai.core.annotation.ExcelCellGroup;
import com.mihai.core.annotation.ExcelColumn;

public class AddressesRow {

    @ExcelColumn("Id")
    private Integer id;

    @ExcelCellGroup(value = "Address A")
    private Address addressA;

    @ExcelCellGroup(value = "Address B")
    private Address addressB;

    public AddressesRow() {
    }

    public AddressesRow(Integer id, Address addressA, Address addressB) {
        this.id = id;
        this.addressA = addressA;
        this.addressB = addressB;
    }

    public Integer getId() {
        return id;
    }

    public Address getAddressA() {
        return addressA;
    }

    public Address getAddressB() {
        return addressB;
    }
}
