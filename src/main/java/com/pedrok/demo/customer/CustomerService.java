package com.pedrok.demo.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepositoryInterface customerRepository;

    @Autowired
    public CustomerService(CustomerRepositoryInterface customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getCustomers() {
        return customerRepository.getCustomers();
    }
}
