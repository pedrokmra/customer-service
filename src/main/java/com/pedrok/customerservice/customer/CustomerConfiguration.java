package com.pedrok.customerservice.customer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerConfiguration {
    @Value("${app.useCustomerFakeRepository:false}")
    private boolean useCustomerFakeRepository;

    @Bean
    public CustomerRepositoryInterface customerRepositoryInterface() {
        // System.out.println("useCustomerFakeRepository = " + useCustomerFakeRepository);

        return new CustomerFakeRepository();
    }
}
