package net.hub4u.ebgsys.services.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.hub4u.ebgsys.entities.Supplier;
import net.hub4u.ebgsys.repositories.SupplierRepository;
import net.hub4u.ebgsys.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    SupplierRepository supplierRepository;

    @Override
    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public List<Supplier> fetchAllSuppliers() {
        Iterable<Supplier> supplierIterable = supplierRepository.findAll();
        List<Supplier> suppliers = new ArrayList<>();
        supplierIterable.forEach(suppliers::add);
        return suppliers;
    }

    @Override
    public Supplier fetchSupplierByReference(String reference) {
        return supplierRepository.findByReference(reference).orElseThrow(
                () -> new IllegalArgumentException("fetchSupplierByReference() - Failed finding Supplier with reference :" + reference));
    }

    @Override
    public Supplier updateSupplier(Supplier supplier) {
        Supplier currentSupplier = fetchSupplierByReference(supplier.getReference());

        currentSupplier.setAddress(supplier.getAddress());
        currentSupplier.setEmail(supplier.getEmail());
        currentSupplier.setName(supplier.getName());
        currentSupplier.setPhone(supplier.getPhone());

        currentSupplier.setContactPerson(supplier.getContactPerson());
        currentSupplier.setCreatedBy(supplier.getCreatedBy());
        currentSupplier.setCreationDate(supplier.getCreationDate());
        currentSupplier.setModificationDate(new Date());
        currentSupplier.setWebSite(supplier.getWebSite());

        currentSupplier.setNextReferenceView(supplier.getNextReferenceView());

        return currentSupplier;
    }

    @Override
    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }

}
