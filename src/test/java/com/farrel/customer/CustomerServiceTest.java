package com.farrel.customer;

import com.farrel.exception.DuplicateResourceException;
import com.farrel.exception.RequestValidationException;
import com.farrel.exception.ResourceNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        // When
        underTest.getAllCustomers();

        // Then
        Mockito.verify(customerDao).findAllCustomers();
    }

    @Test
    void canGetCustomer() {
        // The reason why we're doing this, is because currently the customerDao is a mock.
        // And the mock doesn't know what to do when we invoke a method on it.
        // So hence we have to tell it exactly what to do.
        // In our case, when the mock invokes findCustomerById, we wanted to return this customer

        // Given
        Long id = 10L;
        Customer customer = new Customer(id, "Farrel", "farrel@domain.com", 17);

        Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));

        // When
        Customer actual = underTest.getCustomer(id);

        // Then
        Assertions.assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnEmpty() {
        // Given
        Long id = 10L;
        Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.empty());

        // When
        // Then
        Assertions.assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer Not Found");
    }

    @Test
    void insertCustomer() {
        // Given
        String email = "farrel@domain.com";

        Mockito.when(customerDao.existsByEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("farrel", email, 17);

        // When
        underTest.insertCustomer(request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        Assertions.assertThat(capturedCustomer.getId()).isNull();
        Assertions.assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        Assertions.assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        Assertions.assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenEmailExistsWhileInsertingACustomer() {
        // Given
        String email = "farrel@domain.com";

        Mockito.when(customerDao.existsByEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("farrel", email, 17);

        // When
        Assertions.assertThatThrownBy(() -> underTest.insertCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");

        // Then
        Mockito.verify(customerDao, Mockito.never()).insertCustomer(Mockito.any());
    }

    @Test
    void canUpdateAllCustomerProperties() {
        // Given
        Long id = 10L;
        Customer customer = new Customer(id, "Farrel", "farrel@domain.com", 17);

        Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "putra@domain.com";
        CustomerUpdateRequest request = new CustomerUpdateRequest("Putra", newEmail, 20);

        Mockito.when(customerDao.existsByEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        Assertions.assertThat(capturedCustomer.getId()).isEqualTo(id);
        Assertions.assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        Assertions.assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        Assertions.assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void canUpdateCustomerNameOnly() {
        // Given
        Long id = 10L;
        Customer customer = new Customer(id, "Farrel", "farrel@domain.com", 17);

        Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest("Putra", null, null);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        Assertions.assertThat(capturedCustomer.getId()).isEqualTo(id);
        Assertions.assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        Assertions.assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        Assertions.assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateCustomerEmailOnly() {
        // Given
        Long id = 10L;
        Customer customer = new Customer(id, "Farrel", "farrel@domain.com", 17);

        Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(null, "putra@domain.com", null);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        Assertions.assertThat(capturedCustomer.getId()).isEqualTo(id);
        Assertions.assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        Assertions.assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        Assertions.assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void willThrowWhenUpdatingCustomerEmailThatAlreadyTaken() {
        // Given
        Long id = 10L;
        Customer customer = new Customer(id, "Farrel", "farrel@domain.com", 17);

        Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "putra@domain.com";
        CustomerUpdateRequest request = new CustomerUpdateRequest(null, newEmail, null);

        Mockito.when(customerDao.existsByEmail(newEmail)).thenReturn(true);

        // When
        Assertions.assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");

        // Then
        Mockito.verify(customerDao, Mockito.never()).updateCustomer(Mockito.any());
    }

    @Test
    void canUpdateCustomerAgeOnly() {
        // Given
        Long id = 10L;
        Customer customer = new Customer(id, "Farrel", "farrel@domain.com", 17);

        Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(null, null, 21);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        Assertions.assertThat(capturedCustomer.getId()).isEqualTo(id);
        Assertions.assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        Assertions.assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        Assertions.assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        // Given
        Long id = 10L;
        Customer customer = new Customer(id, "Farrel", "farrel@domain.com", 17);

        Mockito.when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(customer.getName(), customer.getEmail(), customer.getAge());

        // When
        Assertions.assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No data changes found");

        // Then
        Mockito.verify(customerDao, Mockito.never()).updateCustomer(Mockito.any());
    }

    @Test
    void deleteCustomer() {
        // Given
        Long id = 2L;
        Mockito.when(customerDao.existsById(id)).thenReturn(true);

        // When
        underTest.deleteCustomer(id);

        // Then
        Mockito.verify(customerDao).deleteCustomer(id);
    }

    @Test
    void willThrowWhenIdNotExistsWhileDeletingACustomer() {
        // Given
        Long id = 2L;
        Mockito.when(customerDao.existsById(id)).thenReturn(false);

        // When
        Assertions.assertThatThrownBy(() -> underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] is not found".formatted(id));

        // Then
        Mockito.verify(customerDao, Mockito.never()).deleteCustomer(id);
    }
}