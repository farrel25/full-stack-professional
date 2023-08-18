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

        Customer alex = new Customer(1L, "Alex", "alex@domain.com", 21);
        customers.add(alex);

        Customer jamila = new Customer(2L, "Jamila", "jamila@domain.com", 19);
        customers.add(jamila);
    }

    @Override
    public List<Customer> findAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> findCustomerById(Long id) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.stream()
                .filter(c -> c.getId().equals(customer.getId()))
                .findFirst()
                .ifPresent(c -> {
                    c.setName(customer.getName());
                    c.setEmail(customer.getEmail());
                    c.setAge(customer.getAge());
                });
    }

    @Override
    public boolean existsById(Long id) {
        return customers.stream().anyMatch(customer -> customer.getId().equals(id));
    }

    @Override
    public boolean existsByEmail(String email) {
        return customers.stream().anyMatch(customer -> customer.getEmail().equals(email));
    }

    @Override
    public void deleteCustomer(Long id) {
        customers.removeIf(customer -> customer.getId().equals(id));
    }
}
