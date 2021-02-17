package net.hub4u.ebgsys.entities;

public enum SaleType {

    WHOLESALE ("WHOLESALE"),
    RETAIL ("RETAIL");

    private final String value;

    private SaleType(String value) {
        this.value =value;
    }

}
