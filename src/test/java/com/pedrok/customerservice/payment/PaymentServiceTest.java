package com.pedrok.customerservice.payment;

import com.pedrok.customerservice.customer.Customer;
import com.pedrok.customerservice.customer.CustomerService;
import com.pedrok.customerservice.exception.NotFoundException;
import com.pedrok.customerservice.message.Message;
import com.pedrok.customerservice.message.MessageSend;
import com.pedrok.customerservice.message.MessageSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.then;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private CustomerService customerService;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CardPaymentCharger cardPaymentCharger;
    @Mock
    private MessageSender messageSender;
    @InjectMocks
    private PaymentService paymentService;

    @Test
    void itShouldChargeCard() {
        // GIVEN
        Long customerId = 1L;
        Customer customer = Customer.builder()
                .id(customerId)
                .name("test")
                .password("123")
                .email("test@test.com")
                .phoneNumber("55519999999").build();

        Payment payment = Payment.builder()
                .amount(new BigDecimal("100.00"))
                .currency(Currency.USD)
                .source("card123")
                .description("expenses")
                .build();

        String message = payment.getCurrency() + ": " + payment.getAmount() + " debitted";

        // WHEN
        when(customerService.getCustomer(customerId)).thenReturn(customer);

        when(cardPaymentCharger.chargeCard(
                        payment.getSource(),
                        payment.getAmount(),
                        payment.getCurrency(),
                        payment.getDescription()
                )).thenReturn(new CardPaymentCharge(true));

        when(messageSender.send(new Message(customer.getPhoneNumber(), message)))
                .thenReturn(new MessageSend(true));

        paymentService.chargeCard(customerId, payment);

        // THEN
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);
        then(paymentRepository).should().save(paymentArgumentCaptor.capture());
        Payment paymentArgumentCaptorValue = paymentArgumentCaptor.getValue();

        assertThat(paymentArgumentCaptorValue).isEqualTo(payment);
        assertThat(paymentArgumentCaptorValue.getCustomerId()).isEqualTo(customerId);

        verify(customerService, times(1)).getCustomer(customerId);

        verify(cardPaymentCharger, times(1)).chargeCard(
                payment.getSource(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getDescription()
        );

        verify(paymentRepository, times(1)).save(payment);

        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        then(messageSender).should().send(messageArgumentCaptor.capture());
        Message messageArgumentCaptorValue = messageArgumentCaptor.getValue();

        assertThat(messageArgumentCaptorValue.message()).isEqualTo(message);
        assertThat(messageArgumentCaptorValue.to()).isEqualTo(customer.getPhoneNumber());
    }

    @Test
    void itShouldThrowWhenCustomerNotFound() {
        // GIVEN
        Long customerId = 1L;
        Payment payment = Payment.builder()
                .amount(new BigDecimal("100.00"))
                .currency(Currency.USD)
                .source("card123")
                .description("expenses")
                .build();

        // WHEN
        when(customerService.getCustomer(customerId)).thenThrow(NotFoundException.class);

        // THEN
        assertThrows(NotFoundException.class, () -> paymentService.chargeCard(customerId, payment));

        then(cardPaymentCharger).shouldHaveNoInteractions();
        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldThrowWhenCurrencyIsNotSupported() {
        // GIVEN
        Currency unsupportedCurrency = Currency.BRL;

        Payment payment = Payment.builder()
                .amount(new BigDecimal("100.00"))
                .currency(unsupportedCurrency)
                .source("card123")
                .description("expenses")
                .build();

        // WHEN
        // THEN
        assertThatThrownBy(() -> paymentService.chargeCard(1L, payment))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("currency " + unsupportedCurrency + " is not supported");

        then(cardPaymentCharger).shouldHaveNoInteractions();
        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldThrowWhenCustomerMessageNotSent() {
        // GIVEN
        Long customerId = 1L;
        Customer customer = Customer.builder()
                .id(customerId)
                .name("test")
                .password("123")
                .email("test@test.com")
                .phoneNumber("55519999999").build();

        Payment payment = Payment.builder()
                .amount(new BigDecimal("100.00"))
                .currency(Currency.USD)
                .source("card123")
                .description("expenses")
                .build();

        String message = payment.getCurrency() + ": " + payment.getAmount() + " debitted";

        // WHEN
        when(customerService.getCustomer(customerId)).thenReturn(customer);

        when(cardPaymentCharger.chargeCard(
                payment.getSource(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getDescription()
        )).thenReturn(new CardPaymentCharge(true));

        when(messageSender.send(new Message(customer.getPhoneNumber(), message)))
                .thenReturn(new MessageSend(false));

        // THEN
        assertThrows(IllegalStateException.class, () -> paymentService.chargeCard(customerId, payment));
    }

    @Test
    void itShouldThrowWhenCardIsNotCharged() {
        // GIVEN
        Long customerId = 1L;

        Payment payment = Payment.builder()
                .amount(new BigDecimal("100.00"))
                .currency(Currency.USD)
                .source("card123")
                .description("expenses")
                .build();

        // WHEN
        when(cardPaymentCharger.chargeCard(
                payment.getSource(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getDescription()
        )).thenReturn(new CardPaymentCharge(false));

        // THEN
        assertThatThrownBy(() -> paymentService.chargeCard(customerId, payment))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("card not debited for customer " + customerId);

        then(paymentRepository).shouldHaveNoInteractions();
    }
}