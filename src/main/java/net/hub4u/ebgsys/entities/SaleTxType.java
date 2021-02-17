package net.hub4u.ebgsys.entities;

public enum SaleTxType {

    CREDIT ("CREDIT"),
    CASH ("CASH");

    private final String value;

    private SaleTxType(String value) {
        this.value =value;
    }

}
