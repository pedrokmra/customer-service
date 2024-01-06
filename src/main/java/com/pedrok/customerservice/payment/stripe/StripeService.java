package com.pedrok.customerservice.payment.stripe;

import com.pedrok.customerservice.payment.CardPaymentCharge;
import com.pedrok.customerservice.payment.CardPaymentCharger;
import com.pedrok.customerservice.payment.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
@ConditionalOnProperty(
        value = "stripe.enabled",
        havingValue = "true"
)
public class StripeService implements CardPaymentCharger {

    private final StripeApi stripeApi;
    private final static RequestOptions requestOptions = RequestOptions.builder()
            .setApiKey("sk_test_Ho24N7La5CVDtbmpjc377lJI")
            .build();

    @Override
    public CardPaymentCharge chargeCard(String cardSource, BigDecimal amount, Currency currency, String description) {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        params.put("source", cardSource);
        params.put("description", description);

        try {
            Charge charge = stripeApi.create(params, requestOptions);
            return new CardPaymentCharge(charge.getPaid());
        } catch (StripeException e) {
            log.error("error make stripe charge");
            throw new IllegalStateException("error make stripe charge", e);
        }
    }
}
