package net.hub4u.ebgsys.repositories;

import net.hub4u.ebgsys.entities.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findByReference(String reference);
}
