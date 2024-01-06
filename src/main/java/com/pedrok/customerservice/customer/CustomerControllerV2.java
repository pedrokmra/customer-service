package com.pedrok.customerservice.customer;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v2/customer")
@AllArgsConstructor
public class CustomerControllerV2 {
    private final CustomerService customerService;

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customer));
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
