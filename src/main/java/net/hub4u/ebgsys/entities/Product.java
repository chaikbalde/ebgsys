package net.hub4u.ebgsys.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
    int minQuantity;
    int grossPriceQuantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
    List<SaleProduct> saleProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
    List<PurchaseProduct> purchaseProducts = new ArrayList<>();

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    StockItem stockItem;

    // Transient fields for the view
    @Transient
    String nextReferenceView;

    public Product(String name, String reference, String description, BigDecimal unitPrice, BigDecimal grossPrice, BigDecimal cost, int grossPriceQuantity, int minQuantity) {
        this.name = name;
        this.reference = reference;
        this.description = description;
        this.unitPrice = unitPrice;
        this.grossPrice = grossPrice;
        this.cost = cost;
        this.grossPriceQuantity = grossPriceQuantity;
        this.minQuantity = minQuantity;
    }
}
