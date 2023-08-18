package com.farrel.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {

    List<Customer> findAllCustomers();

    Optional<Customer> findCustomerById(Long id);

    void insertCustomer(Customer customer);

    void updateCustomer(Customer customer);

    boolean existsById(Long id);

    boolean existsByEmail(String email);

    void deleteCustomer(Long id);
}
