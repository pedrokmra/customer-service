package com.pedrok.customerservice.customer;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
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
        Customer customer = Customer.builder()
                .name("test")
                .password("123")
                .email("email@email.com")
                .phoneNumber("5199221919").build();

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
        String phoneNumber = "5199221919";

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
                "test",
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

    @Test
    void itShouldNotSaveCustomerWhenNameIsNull() {
        // GIVEN
        Customer customer = Customer.builder()
                .name(null)
                .password("123")
                .email("email@email.com")
                .phoneNumber("5199221919").build();

        //WHEN
        //THEN
        assertThatThrownBy(() -> customerRepository.save(customer))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("name must not be empty");
    }

    @Test
    void itShouldNotSaveCustomerWhenPhoneNumberIsNull() {
        // GIVEN
        Customer customer = Customer.builder()
                .name("test")
                .password("123")
                .email("email@email.com")
                .phoneNumber(null).build();

        // WHEN
        // THEN
        assertThatThrownBy(() -> customerRepository.save(customer))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("phoneNumber must not be empty");
    }

    @Test
    void itShouldNotSaveCustomersWithSameEmail() {
        // GIVEN
        List<Customer> customers = List.of(Customer.builder()
                        .name("test")
                        .password("123")
                        .email("email@email.com")
                        .phoneNumber("51992219192").build(),
                Customer.builder()
                        .name("test2")
                        .password("123")
                        .email("email@email.com")
                        .phoneNumber("5199221919").build()
        );

        // WHEN
        // THEN
        assertThatThrownBy(() -> customerRepository.saveAll(customers))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Unique index or primary key violation");
    }

    @Test
    void itShouldNotSaveCustomersWithSamePhoneNumber() {
        // GIVEN
        List<Customer> customers = List.of(Customer.builder()
                        .name("test")
                        .password("123")
                        .email("email1@email.com")
                        .phoneNumber("5199221919").build(),
                Customer.builder()
                        .name("test2")
                        .password("123")
                        .email("emai2l@email.com")
                        .phoneNumber("5199221919").build()
        );

        // WHEN
        // THEN
        assertThatThrownBy(() -> customerRepository.saveAll(customers))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Unique index or primary key violation");
    }
}
