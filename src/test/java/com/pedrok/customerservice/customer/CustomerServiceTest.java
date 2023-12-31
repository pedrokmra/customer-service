package com.pedrok.customerservice.customer;

import com.pedrok.customerservice.exception.NotFoundException;
import com.pedrok.customerservice.utils.PhoneNumberValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.then;
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PhoneNumberValidator phoneNumberValidator;
    @InjectMocks
    private CustomerService customerService;
    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    @Test
    void itShouldGetCustomers() {
        // GIVEN
        List<Customer> customers = List.of(Customer.builder()
                .name("test")
                .password("123")
                .email("email@email.com")
                .phoneNumber("0000").build(),
                Customer.builder()
                        .name("test2")
                        .password("1234")
                        .email("email2@email.com")
                        .phoneNumber("00001").build());

        // WHEN
        when(customerRepository.findAll()).thenReturn(customers);

        // THEN
        assertEquals(customers, customerService.getCustomers());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void itShouldGetCustomer() {
        // GIVEN
        Long id = 1L;

        Customer customer = Customer.builder()
                .id(id)
                .name("test")
                .password("123")
                .email("email@email.com")
                .phoneNumber("0000").build();

        // WHEN
        when(customerRepository.findById(id)).thenReturn(Optional.ofNullable(customer));

        // THEN
        assertEquals(customer, customerService.getCustomer(id));

        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    void itShouldThrowWhenCustomerNotFound() {
        // GIVEN
        Long id = 1L;

        // WHEN
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        // THEN
        assertThatThrownBy(() -> customerService.getCustomer(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("customer with ID " + id + " not found");

        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    void itShouldCreateCustomer() {
        // GIVEN
        String phoneNumber = "0000";

        Customer customer = Customer.builder()
                .name("test")
                .password("123")
                .email("email@email.com")
                .phoneNumber(phoneNumber).build();

        // WHEN
        when(customerRepository.save(customer)).thenReturn(customer);
        when(phoneNumberValidator.validate(phoneNumber)).thenReturn(true);
        customerService.createCustomer(customer);

        // THEN
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue).isEqualTo(customer);

        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void itShouldThrowWhenPhoneNumberIsInvalid() {
        // GIVEN
        String phoneNumber = "0000";

        Customer customer = Customer.builder()
                .name("test")
                .password("123")
                .email("email@email.com")
                .phoneNumber(phoneNumber).build();

        // WHEN
        when(phoneNumberValidator.validate(phoneNumber)).thenReturn(false);

        // THEN
        assertThrows(IllegalStateException.class, () -> customerService.createCustomer(customer));
        verify(customerRepository, times(0)).save(customer);
    }

    @Test
    void itShouldThrowWhenEmailAlreadyExists() {
        // GIVEN
        String phoneNumber = "000";
        Customer customer = Customer.builder()
                .name("test")
                .password("123")
                .email("email@email.com")
                .phoneNumber(phoneNumber).build();

        when(customerRepository.save(customer))
                .thenThrow(new DataIntegrityViolationException("DataIntegrityViolationException"));
        when(phoneNumberValidator.validate(phoneNumber)).thenReturn(true);

        // WHEN
        // THEN
        assertThrows(DataIntegrityViolationException.class, () -> customerService.createCustomer(customer));
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void itShouldThrowWhenPhoneNumberAlreadyExists() {
        // GIVEN
        String phoneNumber = "0000";
        Customer customer = Customer.builder()
                .name("test")
                .password("123")
                .email("email@email.com")
                .phoneNumber(phoneNumber).build();

        when(customerRepository.save(customer))
                .thenThrow(new DataIntegrityViolationException("DataIntegrityViolationException"));

        // WHEN
        when(phoneNumberValidator.validate(phoneNumber)).thenReturn(true);

        // THEN
        assertThrows(DataIntegrityViolationException.class, () -> customerService.createCustomer(customer));
        verify(customerRepository, times(1)).save(customer);
    }
}