package com.farrel.customer;

import com.farrel.AbstractTestcontainers;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

class CustomerJdbcDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJdbcDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJdbcDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void findAllCustomers() {
        // Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                FAKER.number().numberBetween(17, 60)
        );
        underTest.insertCustomer(customer);

        // When
        List<Customer> customers = underTest.findAllCustomers();

        // Then
        Assertions.assertThat(customers).isNotEmpty();
    }

    @Test
    void findCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(17, 60)
        );

        underTest.insertCustomer(customer);

        Long customerId = underTest.findAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Optional<Customer> customerOptional = underTest.findCustomerById(customerId);

        // Then
        Assertions.assertThat(customerOptional).isPresent().hasValueSatisfying(c -> {
            Assertions.assertThat(c.getId()).isEqualTo(customerId);
            Assertions.assertThat(c.getName()).isEqualTo(customer.getName());
            Assertions.assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            Assertions.assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenFindCustomerById() {
        // Given
        Long customerId = -1L;

        // When
        Optional<Customer> customerOptional = underTest.findCustomerById(customerId);

        // Then
        Assertions.assertThat(customerOptional).isEmpty();
    }

    @Test
    void updateCustomerName() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(17, 60)
        );

        underTest.insertCustomer(customer);

        Long customerId = underTest.findAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        String newName = "Foo";

        // When
        customer.setId(customerId);
        customer.setName(newName);

        underTest.updateCustomer(customer);

        // Then
        Optional<Customer> actual = underTest.findCustomerById(customerId);

        Assertions.assertThat(actual).isPresent().hasValueSatisfying(c -> {
            Assertions.assertThat(c.getId()).isEqualTo(customerId);
            Assertions.assertThat(c.getName()).isEqualTo(newName);
            Assertions.assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            Assertions.assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(17, 60)
        );

        underTest.insertCustomer(customer);

        Long customerId = underTest.findAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        String newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        customer.setId(customerId);
        customer.setEmail(newEmail);

        underTest.updateCustomer(customer);

        // Then
        Optional<Customer> actual = underTest.findCustomerById(customerId);

        Assertions.assertThat(actual).isPresent().hasValueSatisfying(c -> {
            Assertions.assertThat(c.getId()).isEqualTo(customerId);
            Assertions.assertThat(c.getName()).isEqualTo(customer.getName());
            Assertions.assertThat(c.getEmail()).isEqualTo(newEmail);
            Assertions.assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerAge() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        Long customerId = underTest.findAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Integer newAge = 25;

        // When
        customer.setId(customerId);
        customer.setAge(newAge);

        underTest.updateCustomer(customer);

        // Then
        Optional<Customer> actual = underTest.findCustomerById(customerId);

        Assertions.assertThat(actual).isPresent().hasValueSatisfying(c -> {
            Assertions.assertThat(c.getId()).isEqualTo(customerId);
            Assertions.assertThat(c.getName()).isEqualTo(customer.getName());
            Assertions.assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            Assertions.assertThat(c.getAge()).isEqualTo(newAge);
        });
    }

    @Test
    void willUpdateAllPropertiesCustomer() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        Long customerId = underTest.findAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Customer update = new Customer();
        update.setId(customerId);
        update.setName("Foo");
        update.setEmail(FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID());
        update.setAge(25);

        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.findCustomerById(customerId);
        Assertions.assertThat(actual).isPresent().hasValue(update);
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        Long customerId = underTest.findAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Customer update = new Customer();
        update.setId(customerId);
        update.setName(customer.getName());
        update.setEmail(customer.getEmail());
        update.setAge(customer.getAge());

        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.findCustomerById(customerId);

        Assertions.assertThat(actual).isPresent().hasValueSatisfying(c -> {
            Assertions.assertThat(c.getId()).isEqualTo(customerId);
            Assertions.assertThat(c.getName()).isEqualTo(customer.getName());
            Assertions.assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            Assertions.assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void existsById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(17, 60)
        );

        underTest.insertCustomer(customer);

        Long customerId = underTest.findAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        boolean actual = underTest.existsById(customerId);

        // Then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    void existsByIdWillReturnFalseWhenIdNotPresent() {
        // Given
        Long customerId = -1L;

        // When
        boolean actual = underTest.existsById(customerId);

        // Then
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    void existsByEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                FAKER.number().numberBetween(17, 60)
        );

        underTest.insertCustomer(customer);

        // When
        boolean actual = underTest.existsByEmail(email);

        // Then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    void existsByEmailReturnsFalseWhenDoeNotExists() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        boolean actual = underTest.existsByEmail(email);

        // Then
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomer() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(17, 60)
        );

        underTest.insertCustomer(customer);

        Long customerId = underTest.findAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        underTest.deleteCustomer(customerId);

        // Then
        Optional<Customer> actual = underTest.findCustomerById(customerId);
        Assertions.assertThat(actual).isNotPresent();
    }
}