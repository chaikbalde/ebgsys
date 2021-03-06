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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sale {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String reference;

    Date saleDate;
    BigDecimal amount;

//        SaleType saleType;

    SaleTxType saleTxType;
//    int quantity;

    // Calculated fields
    boolean paid;
    BigDecimal rest;
    BigDecimal balance;
    BigDecimal paidAmount;

    Date creationDate;
    String createdBy;
    Date modificationDate;
    String modifiedBy;

//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    Product product;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    List<SaleProduct> saleProducts = new ArrayList<>();


    @ManyToOne
    //@JoinColumn(name = "customer_id")
    Customer customer;



    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    List<Payment> payments = new ArrayList<>();

    String customerName;
    String customerPhone;

    // Transient fields for the view
    @Transient
    String saleTypeView;
    @Transient
    BigDecimal unitPriceView;
    @Transient
    String nextReferenceView;
    @Transient
    String customerNameView;

}
