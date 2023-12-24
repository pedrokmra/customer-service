package com.pedrok.customerservice.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/customer")
@Deprecated
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
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
