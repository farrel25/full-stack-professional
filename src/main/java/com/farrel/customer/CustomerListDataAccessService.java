package com.farrel.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository(value = "list")
public class CustomerListDataAccessService implements CustomerDao{

    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();

        Customer alex = new Customer(1, "Alex", "alex@domain.com", 21);
        customers.add(alex);

        Customer jamila = new Customer(2, "Jamila", "jamila@domain.com", 19);
        customers.add(jamila);
    }

    @Override
    public List<Customer> findAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> findCustomerById(Integer id) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst();
    }
}
