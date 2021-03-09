package net.hub4u.ebgsys.services;

import net.hub4u.ebgsys.entities.Customer;

import java.util.List;

public interface CustomerService {

    Customer createCustomer(Customer customer);

    List<Customer> fetchAllCustomers();

    void deleteCustomer(Long id);

    Customer fetchCustomer(Long id);

    Customer fetchCustomerByReference(String reference);

    Customer updateCustomer(Customer customer);
}
