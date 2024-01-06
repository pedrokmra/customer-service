package com.pedrok.customerservice.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedrok.customerservice.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(CustomerControllerV2.class)
public class CustomerControllerV2IntegrationTest {

    @MockBean
    private CustomerService customerService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void itShouldReturnCustomers() throws Exception {
        // GIVEN
        List<Customer> customers = List.of(
                Customer.builder()
                        .id(1L)
                        .name("test1")
                        .password("123")
                        .email("test1@test.com")
                        .phoneNumber("00000").build(),
                Customer.builder()
                        .id(2L)
                        .name("test2")
                        .password("123")
                        .email("test2@test.com")
                        .phoneNumber("00001").build()
        );

        // WHEN
        when(customerService.getCustomers()).thenReturn(customers);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/v2/customer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customers)));
    }

    @Test
    void itShouldReturnCustomer() throws Exception {
        // GIVEN
        Long id = 1L;
        Customer customer = Customer.builder()
                .id(id)
                .name("test1")
                .password("123")
                .email("test1@test.com")
                .phoneNumber("00000").build();

        // WHEN
        when(customerService.getCustomer(id)).thenReturn(customer);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/v2/customer/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customer)));
    }

    @Test
    void itShouldThrowWhenCustomerNotFound() throws Exception {
        // GIVEN
        Long id = 1L;

        String errorMessage = "customer with ID " + id + " not found";

        // WHEN
        when(customerService.getCustomer(id)).thenThrow(new NotFoundException(errorMessage));

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/v2/customer/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value(errorMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zonedDateTime").isNotEmpty());
    }

    @Test
    void itShouldCreateCustomer() throws Exception {
        // GIVEN
        Map<String, String> customer = new HashMap<>();
        customer.put("name", "test1");
        customer.put("password", "12345");
        customer.put("email", "test1@test.com");
        customer.put("phoneNumber", "00000");

        Customer createdCustomer = Customer.builder()
                .id(1L)
                .name("test1")
                .password("12345")
                .email("test1@test.com")
                .phoneNumber("00000").build();


        // WHEN
        when(customerService.createCustomer(any(Customer.class))).thenReturn(createdCustomer);

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/v2/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(createdCustomer)));
    }

    @Test
    void itShouldThrowWhenEmailAlreadyExists() throws Exception {
        String email = "test1@test.com";
        Map<String, String> customer = new HashMap<>();
        customer.put("name", "test1");
        customer.put("password", "12345");
        customer.put("email", email);
        customer.put("phoneNumber", "00000");

        String errorMessage = "email " + email + " already exists";

        // WHEN
        when(customerService.createCustomer(any(Customer.class)))
                .thenThrow(new DataIntegrityViolationException(errorMessage));

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/v2/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value(errorMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value(HttpStatus.CONFLICT.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zonedDateTime").isNotEmpty());
    }
}
