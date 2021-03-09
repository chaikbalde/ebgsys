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

    @Override
    public Customer fetchCustomerByReference(String reference) {
        return customerRepository.findByReference(reference).orElseThrow(
                () -> new IllegalArgumentException("fetchCustomerByReference() - Failed finding Customer with reference :" + reference) );
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        Customer currentCustomer = fetchCustomerByReference(customer.getReference());

        currentCustomer.setCustomerType(customer.getCustomerType());
        currentCustomer.setName(customer.getName());
        currentCustomer.setFirstName(customer.getFirstName());
        currentCustomer.setLastName(customer.getLastName());
        currentCustomer.setAddress(customer.getAddress());
        currentCustomer.setEmail(customer.getEmail());
        currentCustomer.setPhone(customer.getPhone());
        currentCustomer.setTitle(customer.getTitle());
        currentCustomer.setWebSite(customer.getWebSite());
        currentCustomer.setNextReferenceView(customer.getNextReferenceView());
        currentCustomer.setReference(customer.getReference());
        currentCustomer.setSales(customer.getSales());

        return currentCustomer;
    }
}
