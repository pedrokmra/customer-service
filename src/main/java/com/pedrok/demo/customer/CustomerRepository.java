package com.pedrok.demo.customer;

import java.util.Collections;
import java.util.List;

public class CustomerRepository implements CustomerRepositoryInterface {

    @Override
    public List<Customer> getCustomers() {
        // TODO connect to DB
        return Collections.emptyList();
    }
}
