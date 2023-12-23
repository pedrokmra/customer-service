package com.pedrok.demo.customer;

import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository(value = "fake")
public class CustomerFakeRepository implements CustomerRepositoryInterface {
    @Override
    public List<Customer> getCustomers() {
        return Arrays.asList(
                new Customer(1L, "batman"),
                new Customer(2L, "joker")
        );
    }
}
