package com.farrel.customer;

import com.farrel.exception.ResourceNotFound;
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

    public Customer getCustomer(Integer id) {
        return customerDao
                .findCustomerById(id)
                .orElseThrow(() -> new ResourceNotFound("Customer Not Found"));
    }
}
