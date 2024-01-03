package com.pedrok.customerservice.payment;

import com.pedrok.customerservice.customer.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CustomerService customerService;
    private final CardPaymentCharger cardPaymentCharger;

    public void chargeCard(Long customerId, Payment payment) {
        customerService.getCustomer(customerId);

        try {
            Currency.valueOf(String.valueOf(payment.getCurrency()));
        } catch (IllegalArgumentException exception) {
            log.error("currency " + payment.getCurrency() + " is not supported");
            throw exception;
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
