package com.pedrok.customerservice.payment.stripe;

import com.pedrok.customerservice.payment.CardPaymentCharge;
import com.pedrok.customerservice.payment.CardPaymentCharger;
import com.pedrok.customerservice.payment.Currency;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MockStripeService implements CardPaymentCharger {
    @Override
    public CardPaymentCharge chargeCard(String cardSource, BigDecimal amount, Currency currency, String description) {
        return new CardPaymentCharge(true);
    }
}
