package com.farrel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@SpringBootApplication
//@ComponentScan(basePackages = "com.farrel")
//@EnableAutoConfiguration
//@Configuration
@RestController
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/greet")
    public GreetResponse greet(@RequestParam(value = "name", required = false) String name) {
        //return "Hello";
        //return new GreetResponse("Hello");

        String greetMessage = name == null || name.isBlank() ? "Hello" : "Hello " + name;
        return new GreetResponse(
                greetMessage,
                List.of("Java", "Golang", "Javascript"),
                new Person("Alex", 18, 10_000)
        );
    }

    record Person(String name, int age, double savings){}

    record GreetResponse(
            String greet,
            List<String> favProgrammingLanguages,
            Person person
    ) {}



//    static class GreetResponse {
//        private final String greet;
//
//        public GreetResponse(String greet) {
//            this.greet = greet;
//        }
//
//        public String getGreet() {
//            return greet;
//        }
//
//        @Override
//        public String toString() {
//            return "GreetResponse{" +
//                    "greet='" + greet + '\'' +
//                    '}';
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (!(o instanceof GreetResponse that)) return false;
//            return Objects.equals(getGreet(), that.getGreet());
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(getGreet());
//        }
//    }



//    record GreetResponse(String greet) {
//
//        @Override
//        public String toString() {
//            return "GreetResponse{" +
//                    "greet='" + greet + '\'' +
//                    '}';
//        }
//
//        @Override
//            public boolean equals(Object o) {
//            if (this == o) return true;
//            if (!(o instanceof GreetResponse that)) return false;
//            return Objects.equals(greet(), that.greet());
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(greet());
//        }
//    }
}
