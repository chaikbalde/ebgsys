package net.hub4u.ebgsys.repositories;

import net.hub4u.ebgsys.entities.StockItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockItemRepository extends CrudRepository<StockItem, Long> {

    Optional<StockItem> findByProductReference(String productReference);
}
