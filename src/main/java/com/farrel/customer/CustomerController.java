package com.farrel.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerId}")
    public Customer getCustomer(@PathVariable(value = "customerId") Long customerId) {
        return customerService.getCustomer(customerId);
    }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        customerService.insertCustomer(customerRegistrationRequest);
    }

    @PutMapping("/{customerId}")
    public void registerCustomer(
            @PathVariable(value = "customerId") Long customerId,
            @RequestBody CustomerUpdateRequest customerUpdateRequest
    ) {
        customerService.updateCustomer(customerId, customerUpdateRequest);
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable(value = "customerId") Long customerId) {
        customerService.deleteCustomer(customerId);
    }
}
