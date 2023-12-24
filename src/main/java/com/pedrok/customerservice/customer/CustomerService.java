package com.pedrok.customerservice.customer;

import com.pedrok.customerservice.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> {
                    NotFoundException notFoundException = new NotFoundException("customer with ID " + id + " not found");
                    log.error("customer with ID {} not found", id, notFoundException);
                    return notFoundException;
                });
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        Customer customerCreated = customerRepository.save(customer);
        log.info("{} created", customerCreated);
        return customerCreated;
    }
}
