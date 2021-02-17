package net.hub4u.ebgsys.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Vehicle {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String reference;
    String registrationNumber;
    String type;
    String make;
    String description;

    public Vehicle(String reference, String registrationNumber, String type, String make, String description) {
        this.reference = reference;
        this.registrationNumber = registrationNumber;
        this.type = type;
        this.make = make;
        this.description = description;
    }
}
