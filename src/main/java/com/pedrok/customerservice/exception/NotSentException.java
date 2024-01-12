package com.pedrok.customerservice.exception;

public class NotSentException extends RuntimeException {
    public NotSentException(String message) {
        super(message);
    }
}
