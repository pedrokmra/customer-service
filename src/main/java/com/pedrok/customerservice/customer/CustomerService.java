package com.pedrok.customerservice.customer;

import com.pedrok.customerservice.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;
    private static final String DB_UK_EMAIL_ERROR =
            "PUBLIC.CONSTRAINT_INDEX_6 ON PUBLIC.CUSTOMERS(EMAIL NULLS FIRST)";
    private static final String DB_UK_PHONE_NUMBER_ERROR =
            "PUBLIC.CONSTRAINT_INDEX_62 ON PUBLIC.CUSTOMERS(PHONE_NUMBER NULLS FIRST)";
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
        try {
            Customer customerCreated = customerRepository.save(customer);
            log.info("{} created", customerCreated);
            return customerCreated;
        } catch (DataIntegrityViolationException exception) {
            if (exception.getMessage().contains(DB_UK_EMAIL_ERROR)) {
                log.error("email {} already exists: {}", customer.getEmail(), exception.getMessage());
                throw new DataIntegrityViolationException("email " + customer.getEmail() + " already exists");
            } else if (exception.getMessage().contains(DB_UK_PHONE_NUMBER_ERROR)) {
                log.error("phoneNumber {} already exists: {}", customer.getPhoneNumber(), exception.getMessage());
                throw new DataIntegrityViolationException("phoneNumber " + customer.getPhoneNumber() + " already exists");
            } else {
                log.error("DataIntegrityViolationException: {}", exception.getMessage());
                throw new DataIntegrityViolationException(exception.getMessage());
            }
        }
    }
}
