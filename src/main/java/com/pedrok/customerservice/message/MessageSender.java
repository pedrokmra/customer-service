package com.pedrok.customerservice.message;

public interface MessageSender {
    MessageSend send(Message message);
}
