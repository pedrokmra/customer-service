package com.pedrok.customerservice.payment;

import com.pedrok.customerservice.customer.CustomerService;
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

    // TODO load from properties file
    private static final List<Currency> ACCEPTED_CURRENCIES = List.of(Currency.USD, Currency.EUR);

    public void chargeCard(Long customerId, Payment payment) {
        customerService.getCustomer(customerId);

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

        // TODO: SEND SMS
    }
}
