package com.pedrok.customerservice.customer;

import com.pedrok.customerservice.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> {
                    NotFoundException notFoundException = new NotFoundException("customer with ID " + id + " not found");
                    LOGGER.error("customer with ID {} not found", id, notFoundException);
                    return notFoundException;
                });
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        Customer customerCreated = customerRepository.save(customer);
        LOGGER.info("{} created", customerCreated);
        return customerCreated;
    }
}
