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
import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;
    String reference;
    String contactPerson;

    String address;
    String phone;
    String email;
    String webSite;

    Date creationDate;
    String createdBy;
    Date modificationDate;
    String modifiedBy;

    public Supplier(String name, String reference, String contactPerson, String address, String phone, String email, String webSite) {
        this.name = name;
        this.reference = reference;
        this.contactPerson = contactPerson;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.webSite = webSite;
    }
}
