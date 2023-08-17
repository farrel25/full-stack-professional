package com.farrel.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {

    List<Customer> findAllCustomers();

    Optional<Customer> findCustomerById(Integer id);

    void insertCustomer(Customer customer);

    void updateCustomer(Customer customer);

    boolean existsById(Integer id);

    boolean existsByEmail(String email);

    void deleteCustomer(Integer id);
}
