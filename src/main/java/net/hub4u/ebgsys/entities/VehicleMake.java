package net.hub4u.ebgsys.entities;

public enum VehicleMake {

    TRUCK ("DAF"),
    IVECO ("IVECO"),
    VAN ("MAGIRUS"),
    MAN ("MAN"),
    MERCEDES ("MERCEDES"),
    NISSAN ("NISSAN"),
    RENAULT ("RENAULT"),
    PICKUP ("SCANIA"),
    TOYOTA ("TOYOTA"),
    MINIVAN ("VOLKSWAGEN");

    private final String value;

    private VehicleMake(String value) {
        this.value =value;
    }

}
