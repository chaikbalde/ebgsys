package net.hub4u.ebgsys.entities;

public enum StockItemStatus {


    AVAILABLE ("AVAILABLE"),
    WARNING ("WARNING"),
    UNAVAILABLE ("UNAVAILABLE");

    private final String value;

    StockItemStatus(String value) {
        this.value =value;
    }

}
