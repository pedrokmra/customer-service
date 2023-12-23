package com.pedrok.demo.customer;

import com.pedrok.demo.exception.NotFoundException;
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

    public Customer getCustomer(Long id) {
        return customerRepository.getCustomers().stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst().orElseThrow(() -> new NotFoundException("customer with ID " + id + " not found"));
    }
}
