package net.hub4u.ebgsys.entities;

public enum CustomerType {

    PERSON ("PERSON"),
    COMPANY("COMPANY");

    private final String value;

    private CustomerType(String value) {
        this.value =value;
    }

//    public static CustomerType fromValue(String value) {
//        for (CustomerType customerType : values()) {
//            if (customerType.value.equalsIgnoreCase(value)) {
//                return customerType;
//            }
//        }
//        throw new IllegalArgumentException(MessageFormat.format("{0} not found with the value: {1} in [{2}]", CustomerType.class, value, values()));
//    }


}
