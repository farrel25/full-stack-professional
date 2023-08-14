package com.farrel.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository(value = "jpa")
public class CustomerJpaDataAccessService implements CustomerDao {

    private final CustomerRepository customerRepository;

    public CustomerJpaDataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findCustomerById(Integer id) {
        return customerRepository.findById(id);
    }
}
