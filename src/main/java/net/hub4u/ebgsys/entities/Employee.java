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
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String reference;

    String firstName;
    String lastName;
    Date birthDate;

    String address;
    String phone;
    String email;

    String position;

    Date creationDate;
    String createdBy;
    Date modificationDate;
    String modifiedBy;

    // Transient fields for the view
    @Transient
    String nextReferenceView;

    public Employee(String reference, String firstName, String lastName, Date birthDate, String address, String phone, String email, String position) {
        this.reference = reference;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.position = position;
    }
}
