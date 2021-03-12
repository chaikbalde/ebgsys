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
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String reference;

    Date purchaseDate;
    BigDecimal amount;

    Date creationDate;
    String createdBy;
    Date modificationDate;
    String modifiedBy;

    @ManyToOne
    Supplier supplier;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.PERSIST)
    List<PurchaseProduct> purchaseProducts = new ArrayList<>();


    @Transient
    String nextReferenceView;





}
