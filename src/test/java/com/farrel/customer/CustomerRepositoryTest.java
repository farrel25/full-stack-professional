package com.farrel.customer;

import com.farrel.AbstractTestcontainers;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;


//@SpringBootTest // the wrong way, because this will load all beans that we don't need for our test
@DataJpaTest // this will load anything that our jpa components need in order to run
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // configuration for no embedded database, then it will connect to database that we use on properties
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
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

        underTest.save(customer);

        // When
        boolean actual = underTest.existsByEmail(email);

        // Then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    void existsByEmailFalseWhenEmailNotPresent() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        boolean actual = underTest.existsByEmail(email);

        // Then
        Assertions.assertThat(actual).isFalse();
    }
}