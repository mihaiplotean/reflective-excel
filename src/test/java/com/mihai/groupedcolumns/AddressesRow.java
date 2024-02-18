package com.mihai.groupedcolumns;

import com.mihai.annotation.ExcelCellGroup;
import com.mihai.annotation.ExcelColumn;

public class AddressesRow {

    @ExcelColumn(name = "Id")
    private Integer id;

    @ExcelCellGroup(name = "Address A")
    private Address addressA;

    @ExcelCellGroup(name = "Address B")
    private Address addressB;

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
