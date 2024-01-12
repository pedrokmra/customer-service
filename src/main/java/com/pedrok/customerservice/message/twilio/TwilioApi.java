package com.pedrok.customerservice.message.twilio;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TwilioApi {
    public static final String ACCOUNT_SID = "ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    public static final String AUTH_TOKEN = "your_auth_token";

    public Message send(Map<String, String> params) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        return Message.creator(
                new PhoneNumber(params.get("to")),
                new PhoneNumber(params.get("from")),
                params.get("message")
        ).create();
    }
}
