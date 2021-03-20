package net.hub4u.ebgsys.entities;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude="product")
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String productReference;
    Date inDate;
    int inQuantity;
    Date outDate;
    int outQuantity;
    int quantity;
    StockItemStatus status;

    @OneToOne
    @JoinColumn(name = "product_id")
    Product product;

    @Transient
    String statusView;

}
