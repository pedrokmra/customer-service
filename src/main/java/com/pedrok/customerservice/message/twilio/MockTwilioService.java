package com.pedrok.customerservice.message.twilio;

import com.pedrok.customerservice.message.Message;
import com.pedrok.customerservice.message.MessageSend;
import com.pedrok.customerservice.message.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(
        value = "twilio.enabled",
        havingValue = "false"
)
public class MockTwilioService implements MessageSender {
    @Override
    public MessageSend send(Message message) {
        log.info("twilio - message {} is send to {} ", message.message(), message.to());
        return new MessageSend(true);
    }
}
