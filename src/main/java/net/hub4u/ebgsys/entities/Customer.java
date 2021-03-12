package net.hub4u.ebgsys.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;
    String reference;

    @Enumerated(EnumType.STRING)
    CustomerType customerType;

    String firstName;
    String lastName;
    String title;

    String address;
    String phone;
    String email;
    String webSite;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST)
    List<Sale> sales = new ArrayList<>();

    // Transient fields for the view
    @Transient
    String nextReferenceView;
    @Transient
    String customerNameView;

    public Customer(String name, String reference, String address, String phone) {
        this.name = name;
        this.reference = reference;
        this.address = address;
        this.phone = phone;
        this.customerType = CustomerType.COMPANY;
    }

    public Customer(String reference, String firstName, String lastName, String address, String phone) {
        this.reference = reference;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.customerType = CustomerType.PERSON;
    }

}
