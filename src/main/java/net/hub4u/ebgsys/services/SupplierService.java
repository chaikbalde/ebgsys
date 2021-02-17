package net.hub4u.ebgsys.services;

import net.hub4u.ebgsys.entities.Supplier;

import java.util.List;

public interface SupplierService {

    Supplier createSupplier(Supplier supplier);

    List<Supplier> fetchAllSuppliers();

    void deleteSupplier(Long id);
}
