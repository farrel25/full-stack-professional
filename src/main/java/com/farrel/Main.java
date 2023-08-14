package com.farrel;

import com.farrel.customer.Customer;
import com.farrel.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

        //ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);
        //printBean(applicationContext);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            Customer alex = new Customer("Alex", "alex@domain.com", 21);
            Customer jamila = new Customer("Jamila", "jamila@domain.com", 19);
            List<Customer> customers = List.of(alex, jamila);

            customerRepository.saveAll(customers);
        };
    }

    @Bean(name = "foo")
    public Foo getFoo() {
        return new Foo("Bar");
    }

    public record Foo(String name){}

    private static void printBean(ConfigurableApplicationContext applicationContext) {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }

}
