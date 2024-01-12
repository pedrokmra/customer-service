package com.pedrok.customerservice.message.twilio;

import com.pedrok.customerservice.exception.NotSentException;
import com.pedrok.customerservice.message.Message;
import com.pedrok.customerservice.message.MessageSend;
import com.pedrok.customerservice.message.MessageSender;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
@ConditionalOnProperty(
        value = "twilio.enabled",
        havingValue = "true"
)
public class TwilioService implements MessageSender {
    private final TwilioApi twilioApi;

    public static final String FROM = "5551999999";

    @Override
    public MessageSend send(Message message) {
        Map<String, String> params = new HashMap<>();
        params.put("to", message.to());
        params.put("from", FROM);
        params.put("message", message.message());

        try {
            twilioApi.send(params);
            log.info("twilio - message {} is send to {} ", message.message(), message.to());
            return new MessageSend(true);
        } catch (Exception exception) {
            log.info("twilio - message {} is not send to {} ", message.message(), message.to());
            throw new NotSentException("error send twilio message");
        }
    }
}
