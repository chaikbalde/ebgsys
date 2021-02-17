package net.hub4u.ebgsys.services;

import net.hub4u.ebgsys.entities.Sale;

import java.util.List;

public interface SaleService {

    Sale createSale(Sale sale);

    List<Sale> fetchAllSales();

    void deleteSale(Long id);
}
