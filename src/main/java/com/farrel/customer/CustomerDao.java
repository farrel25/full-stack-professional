package com.farrel.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {

    List<Customer> findAllCustomers();

    Optional<Customer> findCustomerById(Integer id);
}
