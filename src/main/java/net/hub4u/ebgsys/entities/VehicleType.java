package net.hub4u.ebgsys.entities;

public enum VehicleType {

    TRUCK ("TRUCK"),
    HEAVY_TRUCK ("HEAVY_TRUCK"),
    DUMP_PERSON ("DUMP_TRUCK"),
    VAN ("VAN"),
    PICKUP ("PICKUP"),
    MINIVAN ("MINIVAN"),
    BUS ("BUS");

    private final String value;

    private VehicleType(String value) {
        this.value =value;
    }

}
