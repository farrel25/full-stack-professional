package com.farrel.customer;

import com.farrel.exception.DuplicateResourceException;
import com.farrel.exception.RequestValidationException;
import com.farrel.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.findAllCustomers();
    }

    public Customer getCustomer(Long id) {
        return customerDao
                .findCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Not Found"));
    }

    public void insertCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        // check if email exists
        if (customerDao.existsByEmail(customerRegistrationRequest.email())) {
            throw new DuplicateResourceException("Email already taken");
        }

        // add
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        );

        customerDao.insertCustomer(customer);
    }

    public void updateCustomer(Long customerId, CustomerUpdateRequest customerUpdateRequest) {
        Customer customer = this.getCustomer(customerId);
        boolean changes = false;

        if (customerUpdateRequest.name() != null && !customerUpdateRequest.name().equals(customer.getName())) {
            customer.setName(customerUpdateRequest.name());
            changes = true;
        }

        if (customerUpdateRequest.email() != null && !customerUpdateRequest.email().equals(customer.getEmail())) {
            if (customerDao.existsByEmail(customerUpdateRequest.email())) {
                throw new DuplicateResourceException("Email already taken");
            }

            customer.setEmail(customerUpdateRequest.email());
            changes = true;
        }

        if (customerUpdateRequest.age() != null && !customerUpdateRequest.age().equals(customer.getAge())) {
            customer.setAge(customerUpdateRequest.age());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No data changes found");
        }

        customerDao.updateCustomer(customer);
    }

    public void deleteCustomer(Long customerId) {
        if (!customerDao.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer with id [%s] is not found".formatted(customerId));
        }

        customerDao.deleteCustomer(customerId);
    }
}
