package net.hub4u.ebgsys.repositories;

import net.hub4u.ebgsys.entities.Supplier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends CrudRepository<Supplier, Long> {

    Optional<Supplier> findByReference(String reference);
}
