package com.pedrok.customerservice.customer;

import java.util.Arrays;
import java.util.List;

public class CustomerFakeRepository implements CustomerRepositoryInterface {
    @Override
    public List<Customer> getCustomers() {
        return Arrays.asList(
                new Customer(1L, "batman", "123", "batman@dc.com", "123"),
                new Customer(2L, "joker", "54321", "joker@dcevil.com", "124")
        );
    }
}
