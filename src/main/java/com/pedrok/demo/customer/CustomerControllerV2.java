package com.pedrok.demo.customer;

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
    public Customer createCustomer(@RequestBody Customer customer) {
        System.out.println("POST: " + customer);
        return customer;
    }

    @PutMapping
    public Customer updateCustomer(@RequestBody Customer customer) {
        System.out.println("PUT: " + customer);
        return customer;
    }

    @DeleteMapping("{id}")
    public Long deleteCustomer(@PathVariable Long id) {
        System.out.println("DELETE: customer_id " + id);
        return id;
    }
}
