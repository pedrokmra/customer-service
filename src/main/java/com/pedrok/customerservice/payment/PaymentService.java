package com.pedrok.customerservice.payment;

import com.pedrok.customerservice.customer.Customer;
import com.pedrok.customerservice.customer.CustomerService;
import com.pedrok.customerservice.exception.NotFoundException;
import com.pedrok.customerservice.message.Message;
import com.pedrok.customerservice.message.MessageSend;
import com.pedrok.customerservice.message.MessageSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CustomerService customerService;
    private final CardPaymentCharger cardPaymentCharger;
    private final MessageSender messageSender;

    // TODO load from properties file
    private static final List<Currency> ACCEPTED_CURRENCIES = List.of(Currency.USD, Currency.EUR);

    public void chargeCard(Long customerId, Payment payment) {
        Customer customer = customerService.getCustomer(customerId);

        boolean isCurrencySupported = ACCEPTED_CURRENCIES.contains(payment.getCurrency());
        if (!isCurrencySupported) {
            log.error("currency " + payment.getCurrency() + " is not supported");
            throw new IllegalStateException("currency " + payment.getCurrency() + " is not supported");
        }

        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(
                payment.getSource(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getDescription()
        );

        if (!cardPaymentCharge.isCardDebited()) {
            log.error("card not debited for customer {} ", customerId);
            throw new IllegalStateException("card not debited for customer " + customerId);
        }

        payment.setCustomerId(customerId);
        paymentRepository.save(payment);

        String message = payment.getCurrency() + ": " + payment.getAmount() + " debitted";
        MessageSend messageSend = messageSender.send(new Message(customer.getPhoneNumber(), message));

        if (!messageSend.isSend()) {
            log.error("message {} not sent for customer {} ", message, customerId);
            throw new IllegalStateException("message " + message + " not sent for customer " + customerId);
        }
    }

    public Payment getPayment(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> {
                    NotFoundException notFoundException = new NotFoundException("payment with ID " + id + "not found");
                    log.error("payment with ID " + id + "not found");
                    return notFoundException;
                });
    }
}
