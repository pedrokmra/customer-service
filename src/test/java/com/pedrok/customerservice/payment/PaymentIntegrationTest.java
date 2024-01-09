package com.pedrok.customerservice.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void itShouldCreatePayment() throws Exception {
        // GIVEN
        Map<String, String> customer = new HashMap<>();
        customer.put("name", "test1");
        customer.put("password", "12345");
        customer.put("email", "test1@test.com");
        customer.put("phoneNumber", "5551992817676");

        ResultActions createCustomerResultActions = mockMvc.perform(post("/v2/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(customer))));

        Long customerId =
                objectMapper.readTree
                        (
                        createCustomerResultActions
                                .andReturn().getResponse().getContentAsString()
                        ).get("id")
                        .asLong();

        Long id = 1L;

        Payment payment = Payment.builder()
                .id(id)
                .customerId(customerId)
                .amount(new BigDecimal("100.00"))
                .currency(Currency.USD)
                .source("card123")
                .description("expenses")
                .build();

        // WHEN
        ResultActions createPaymentResultActions = mockMvc.perform(post("/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(payment))));

        ResultActions getPaymentResultActions = mockMvc.perform(get("/v1/payment/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(payment))))
                .andDo(MockMvcResultHandlers.print());

        // THEN
        createCustomerResultActions.andExpect(status().isCreated());

        createPaymentResultActions.andExpect(status().isCreated());

        getPaymentResultActions.andExpect(status().isOk());
        getPaymentResultActions
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(payment)));

        // TODO: Ensure sms is delivered
    }

    @Test
    void itShouldThrowWhenCustomerNotFound() throws Exception {
        // GIVEN
        Payment payment = Payment.builder()
                .id(1L)
                .customerId(10L)
                .amount(new BigDecimal("100.00"))
                .currency(Currency.USD)
                .source("card123")
                .description("expenses")
                .build();

        // WHEN
        ResultActions createPaymentResultActions = mockMvc.perform(post("/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(payment))));

        // THEN
        createPaymentResultActions.andExpect(status().isNotFound());

        createPaymentResultActions
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]")
                        .value("customer with ID " + payment.getCustomerId() + " not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zonedDateTime").isNotEmpty());
    }

    @Test
    void itShouldThrowWhenAmountIsNegative() throws Exception {
        // GIVEN
        Payment payment = Payment.builder()
                .id(1L)
                .customerId(10L)
                .amount(new BigDecimal("-100.00"))
                .currency(Currency.USD)
                .source("card123")
                .description("expenses")
                .build();

        // WHEN
        ResultActions createPaymentResultActions = mockMvc.perform(post("/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(payment))));

        // THEN
        createPaymentResultActions.andExpect(status().isBadRequest());

        createPaymentResultActions.andExpect(
                MockMvcResultMatchers.jsonPath("$.message[0]").value("amount must be positive")
        );
    }

    @Test
    void itShouldThrowWhenAmountIsZero() throws Exception {
        // GIVEN
        Payment payment = Payment.builder()
                .id(1L)
                .customerId(10L)
                .amount(BigDecimal.ZERO)
                .currency(Currency.USD)
                .source("card123")
                .description("expenses")
                .build();

        // WHEN
        ResultActions createPaymentResultActions = mockMvc.perform(post("/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(payment))));

        // THEN
        createPaymentResultActions.andExpect(status().isBadRequest());

        createPaymentResultActions.andExpect(
                MockMvcResultMatchers.jsonPath("$.message[0]").value("amount must be positive")
        );
    }

    @Test
    void itShouldThrowWhenAmountIsNull() throws Exception {
        // GIVEN
        Payment payment = Payment.builder()
                .id(1L)
                .customerId(1L)
                .currency(Currency.USD)
                .source("card123")
                .description("expenses")
                .build();

        // WHEN
        ResultActions createPaymentResultActions = mockMvc.perform(post("/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(payment))));

        // THEN
        createPaymentResultActions.andExpect(status().isBadRequest());

        createPaymentResultActions.andExpect(
                MockMvcResultMatchers.jsonPath("$.message[0]").value("amount must not be null")
        );
    }

    @Test
    void itShouldThrowWhenSourcetIsEmpty() throws Exception {
        // GIVEN
        Payment payment = Payment.builder()
                .id(1L)
                .customerId(1L)
                .amount(new BigDecimal("10.00"))
                .currency(Currency.USD)
                .source(" ")
                .description("expenses")
                .build();

        // WHEN
        ResultActions createPaymentResultActions = mockMvc.perform(post("/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(payment))));

        // THEN
        createPaymentResultActions.andExpect(status().isBadRequest());

        createPaymentResultActions.andExpect(
                MockMvcResultMatchers.jsonPath("$.message[0]").value("source must not be empty")
        );
    }
}
