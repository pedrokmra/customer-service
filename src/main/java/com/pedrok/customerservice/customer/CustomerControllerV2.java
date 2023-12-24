package com.pedrok.customerservice.customer;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v2/customer")
public class CustomerControllerV2 {
    private final CustomerService customerService;

    @Autowired
    public CustomerControllerV2(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    public Customer createCustomer(@Valid @RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @PutMapping
    public Customer updateCustomer(@Valid @RequestBody Customer customer) {
        System.out.println("PUT: " + customer);
        return customer;
    }

    @DeleteMapping("{id}")
    public Long deleteCustomer(@PathVariable Long id) {
        System.out.println("DELETE: customer_id " + id);
        return id;
    }
}