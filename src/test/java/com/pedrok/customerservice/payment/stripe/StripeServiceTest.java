package com.pedrok.customerservice.payment.stripe;

import com.pedrok.customerservice.payment.CardPaymentCharge;
import com.pedrok.customerservice.payment.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StripeServiceTest {
    @Mock
    private StripeApi stripeApi;
    @InjectMocks
    private StripeService stripeService;

    @Test
    void itShouldChargeCard() throws StripeException {
        // GIVEN
        String cardSource = "0x0x0x";
        BigDecimal amount = new BigDecimal("10.00");
        Currency currency = Currency.USD;
        String description = "expenses";

        Charge charge = new Charge();
        charge.setPaid(true);

        // WHEN
        when(stripeApi.create(anyMap(), any())).thenReturn(charge);

        CardPaymentCharge cardPaymentCharge = stripeService.chargeCard(cardSource, amount, currency, description);

        // THEN
        ArgumentCaptor<Map<String, Object>> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<RequestOptions> optionsArgumentCaptor = ArgumentCaptor.forClass(RequestOptions.class);
        then(stripeApi).should().create(mapArgumentCaptor.capture(), optionsArgumentCaptor.capture());

        Map<String, Object> requestMap = mapArgumentCaptor.getValue();
        assertThat(requestMap.keySet()).hasSize(4);
        assertThat(requestMap.get("amount")).isEqualTo(amount);
        assertThat(requestMap.get("currency")).isEqualTo(currency);
        assertThat(requestMap.get("source")).isEqualTo(cardSource);
        assertThat(requestMap.get("description")).isEqualTo(description);

        RequestOptions options = optionsArgumentCaptor.getValue();
        assertThat(options).isNotNull();

        assertThat(cardPaymentCharge).isNotNull();
        assertThat(cardPaymentCharge.isCardDebited()).isTrue();
    }

    @Test
    void itShouldNotChargeWhenApiThrow() throws StripeException {
        // GIVEN
        String cardSource = "0x0x0x";
        BigDecimal amount = new BigDecimal("10.00");
        Currency currency = Currency.USD;
        String description = "expenses";

        StripeException stripeException = mock(StripeException.class);

        // WHEN
        when(stripeApi.create(anyMap(), any())).thenThrow(stripeException);

        // THEN
        assertThatThrownBy(() -> stripeService.chargeCard(cardSource, amount, currency, description))
                .isInstanceOf(IllegalStateException.class)
                .hasRootCause(stripeException)
                .hasMessageContaining("error make stripe charge");
    }
}