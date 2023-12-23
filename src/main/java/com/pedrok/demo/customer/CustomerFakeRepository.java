package com.pedrok.demo.customer;

import java.util.Arrays;
import java.util.List;

public class CustomerFakeRepository implements CustomerRepositoryInterface {
    @Override
    public List<Customer> getCustomers() {
        return Arrays.asList(
                new Customer(1L, "batman"),
                new Customer(2L, "joker")
        );
    }
}
