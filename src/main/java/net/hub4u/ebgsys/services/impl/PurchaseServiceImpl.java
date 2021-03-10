package net.hub4u.ebgsys.services.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.hub4u.ebgsys.entities.Purchase;
import net.hub4u.ebgsys.repositories.PurchaseRepository;
import net.hub4u.ebgsys.services.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    PurchaseRepository purchaseRepository;

    @Override
    public Purchase createSale(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    @Override
    public List<Purchase> fetchAllPurchases() {
        Iterable<Purchase> purchaseIterable = purchaseRepository.findAll();
        List<Purchase> purchases = new ArrayList<>();
        purchaseIterable.forEach(purchases::add);
        return purchases;
    }

    @Override
    public void deletePurchase(Long id) {
        purchaseRepository.deleteById(id);
    }

    @Override
    public Purchase fetchPurchase(Long id) {
        return purchaseRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("fetchPurchase() - Failed to find Purchase with id:  " + id));
    }
}
