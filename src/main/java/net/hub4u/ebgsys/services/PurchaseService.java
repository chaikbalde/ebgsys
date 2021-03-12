package net.hub4u.ebgsys.services;

import net.hub4u.ebgsys.entities.Purchase;

import java.util.List;

public interface PurchaseService {

    Purchase createPurchase(Purchase purchase);

    List<Purchase> fetchAllPurchases();

    void deletePurchase(Long id);

    Purchase fetchPurchase(Long id);

}
