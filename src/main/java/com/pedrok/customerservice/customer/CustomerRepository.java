package com.pedrok.customerservice.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query(
            value = "SELECT ID, NAME, PASSWORD, EMAIL, PHONE_NUMBER " +
                    "  FROM CUSTOMERS C " +
                    " WHERE C.PHONE_NUMBER = :phone_number",
            nativeQuery = true
    )
    Optional<Customer> selectCustomerByPhoneNumber(@Param("phone_number") String phoneNumber);
}
