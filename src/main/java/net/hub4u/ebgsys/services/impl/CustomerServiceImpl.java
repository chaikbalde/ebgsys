package net.hub4u.ebgsys.services.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.hub4u.ebgsys.entities.Customer;
import net.hub4u.ebgsys.entities.Product;
import net.hub4u.ebgsys.repositories.CustomerRepository;
import net.hub4u.ebgsys.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;


    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> fetchAllCustomers() {
        Iterable<Customer> customerIterable = customerRepository.findAll();
        List<Customer> customers = new ArrayList<>();
        customerIterable.forEach(customers::add);
        return customers;
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Customer fetchCustomer(Long id) {
        return customerRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("fetchCustomer() - Failed finding Customer with Id:" + id) );
    }

}
