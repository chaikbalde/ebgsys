package net.hub4u.ebgsys.repositories;

import net.hub4u.ebgsys.entities.Purchase;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
}
