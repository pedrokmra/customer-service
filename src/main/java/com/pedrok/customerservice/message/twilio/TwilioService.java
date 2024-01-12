package com.pedrok.customerservice.message.twilio;

import com.pedrok.customerservice.exception.NotSentException;
import com.pedrok.customerservice.message.MessageSend;
import com.pedrok.customerservice.message.MessageSender;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(
        value = "twilio.enabled",
        havingValue = "true"
)
public class TwilioService implements MessageSender {
    public static final String ACCOUNT_SID = "ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    public static final String AUTH_TOKEN = "your_auth_token";
    public static final String FROM = "5551999999";

    @Override
    public MessageSend send(com.pedrok.customerservice.message.Message message) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            Message messageTwilio = Message.creator(
                            new PhoneNumber(message.to()),
                            new PhoneNumber(FROM),
                            message.message()
                    ).create();

            log.info("twilioId: {} - message {} is send to {} ",
                    messageTwilio.getSid(), message.message(), message.to());

            return new MessageSend(true);
        } catch (Exception exception) {
            log.info("twilio - message {} is not send to {} ", message.message(), message.to());
            throw new NotSentException("error send twilio message");
        }
    }
}
