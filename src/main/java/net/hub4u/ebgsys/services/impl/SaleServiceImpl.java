package net.hub4u.ebgsys.services.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.hub4u.ebgsys.entities.Sale;
import net.hub4u.ebgsys.repositories.SaleRepository;
import net.hub4u.ebgsys.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaleServiceImpl implements SaleService {

    @Autowired
    SaleRepository saleRepository;

    @Override
    public Sale createSale(Sale sale) {
        return saleRepository.save(sale);
    }

    @Override
    public List<Sale> fetchAllSales() {
        Iterable<Sale>  productIterable = saleRepository.findAll();
        List<Sale> sales   = new ArrayList<>();
        productIterable.forEach(sales::add);
        return sales;
    }

    @Override
    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }

    @Override
    public Sale fetchSale(Long id) {
        return saleRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("fetchSale() - Failed finding Sale with Id:" + id) );
    }
}
