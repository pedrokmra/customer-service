package com.pedrok.customerservice.message.twilio;

import com.pedrok.customerservice.exception.NotSentException;
import com.pedrok.customerservice.message.Message;
import com.pedrok.customerservice.message.MessageSend;
import com.twilio.exception.ApiException;
import com.twilio.exception.TwilioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TwilioServiceTest {

    @Mock
    private TwilioApi twilioApi;
    @InjectMocks
    TwilioService twilioService;

    @Test
    void itShouldSend() {
        // GIVEN
        Message message = new Message("5199999", "message");

        // WHEN
        when(twilioApi.send(anyMap())).thenReturn(mock(com.twilio.rest.api.v2010.account.Message.class));
        MessageSend response = twilioService.send(message);

        // THEN
        assertThat(response).isEqualTo(new MessageSend(true));
    }

    @Test
    void itShouldNotSendWhenTwilioApiThrow() {
        // GIVEN
        Message message = new Message("5199999", "message");

        // WHEN
        when(twilioApi.send(anyMap())).thenThrow(mock(TwilioException.class));

        // THEN
        assertThatThrownBy(() -> twilioService.send(message))
                .isInstanceOf(NotSentException.class)
                .hasMessageContaining("error send twilio message");
    }
}