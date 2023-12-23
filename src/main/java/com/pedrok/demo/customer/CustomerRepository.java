package com.pedrok.demo.customer;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@Primary
public class CustomerRepository implements CustomerRepositoryInterface {

    @Override
    public List<Customer> getCustomers() {
        // TODO connect to DB
        return Collections.emptyList();
    }
}
