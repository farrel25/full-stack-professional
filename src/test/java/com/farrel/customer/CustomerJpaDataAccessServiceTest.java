package com.farrel.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJpaDataAccessServiceTest {

    private CustomerJpaDataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        // Because we use the @Mock annotation, we need to initialize the mock itself.
        // This openMocks will return Autocloseable that used for us to close the resource after each test
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJpaDataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void findAllCustomers() {
        // We want to make sure that customerRepository.findAll() inside findAllCustomers() is invoked

        // When
        underTest.findAllCustomers();

        // Then
        Mockito.verify(customerRepository).findAll();
    }

    @Test
    void findCustomerById() {
        // We want to make sure that the parameter value from the findCustomerById method is actually passed inside customerRepository.findById() method

        // Given
        Long id = 1L;

        // When
        underTest.findCustomerById(id);

        // Then
        Mockito.verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = new Customer(1L, "Farrel", "farrel@domain.com", 2);

        // When
        underTest.insertCustomer(customer);

        // Then
        Mockito.verify(customerRepository).save(customer);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = new Customer(1L, "Farrel", "farrel@domain.com", 2);

        // When
        underTest.updateCustomer(customer);

        // Then
        Mockito.verify(customerRepository).save(customer);
    }

    @Test
    void existsById() {
        // Given
        Long id = 1L;

        // When
        underTest.existsById(id);

        // Then
        Mockito.verify(customerRepository).existsById(id);
    }

    @Test
    void existsByEmail() {
        // Given
        String email = "name@domain.com";

        // When
        underTest.existsByEmail(email);

        // Then
        Mockito.verify(customerRepository).existsByEmail(email);
    }

    @Test
    void deleteCustomer() {
        // Given
        Long id = 1L;

        // When
        underTest.deleteCustomer(id);

        // Then
        Mockito.verify(customerRepository).deleteById(id);
    }
}