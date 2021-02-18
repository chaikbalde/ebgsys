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
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;
    String reference;
    String description;
    BigDecimal unitPrice;
    BigDecimal grossPrice;
    BigDecimal cost;

    @OneToMany(mappedBy = "product")
    List<Sale> sales = new ArrayList<>();

    // Transient fields for the view
    @Transient
    String nextReferenceView;

    public Product(String name, String reference, String description, BigDecimal unitPrice, BigDecimal grossPrice, BigDecimal cost) {
        this.name = name;
        this.reference = reference;
        this.description = description;
        this.unitPrice = unitPrice;
        this.grossPrice = grossPrice;
        this.cost = cost;
    }
}
