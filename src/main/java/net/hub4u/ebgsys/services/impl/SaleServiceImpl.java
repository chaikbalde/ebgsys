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

    @Override
    public Sale updateSale(Sale sale) {

        Sale currentSale = fetchSale(sale.getId());
        currentSale.setAmount(sale.getAmount());
        currentSale.setBalance(sale.getBalance());
        currentSale.setCreatedBy(sale.getCreatedBy());
        currentSale.setCreationDate(sale.getCreationDate());
        currentSale.setCustomer(sale.getCustomer());
        currentSale.setCustomerName(sale.getCustomerName());
        currentSale.setCustomerPhone(sale.getCustomerPhone());
        currentSale.setModificationDate(sale.getModificationDate());
        currentSale.setModifiedBy(sale.getModifiedBy());
        currentSale.setPaid(sale.isPaid());
        currentSale.setPaidAmount(sale.getPaidAmount());
        currentSale.setSaleProducts(sale.getSaleProducts());
//        currentSale.setQuantity(sale.getQuantity());
        currentSale.setReference(sale.getReference());
        currentSale.setRest(sale.getRest());
//        currentSale.setSaleType(sale.getSaleType());
        currentSale.setSaleTxType(sale.getSaleTxType());
        currentSale.setSaleDate(sale.getSaleDate());

        return saleRepository.save(currentSale);
    }
}
