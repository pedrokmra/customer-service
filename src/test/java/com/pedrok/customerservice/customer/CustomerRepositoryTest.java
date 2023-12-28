package com.pedrok.customerservice.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        // GIVEN
        String phoneNumber = "5199221919";
        Customer customer = new Customer(
                1L,
                "teste",
                "123",
                "email@email.com",
                phoneNumber);

        // WHEN
        customerRepository.save(customer);

        // THEN
        Optional<Customer> optionalCustomer = customerRepository.selectCustomerByPhoneNumber(phoneNumber);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> assertThat(c).isEqualTo(customer));
    }

    @Test
    void itShouldNotSelectCustomerByPhoneNumber() {
        // GIVEN
        String phoneNumber = "0000";
        // WHEN
        Optional<Customer> optionalCustomer = customerRepository.selectCustomerByPhoneNumber(phoneNumber);
        // THEN
        assertThat(optionalCustomer).isNotPresent();
    }

    @Test
    void itShouldSaveCustomer() {
        // GIVEN
        Long id = 1L;
        Customer customer = new Customer(
                id,
                "teste",
                "123",
                "email@email.com",
                "0000");

        // WHEN
        customerRepository.save(customer);

        // THEN
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> assertThat(c).isEqualTo(customer));
    }
}
