package com.farrel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class Main {

    public static void main(String[] args) {
        //SpringApplication.run(Main.class, args);
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);

        //printBean(applicationContext);
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
