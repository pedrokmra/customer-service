package com.pedrok.customerservice.utils;

import org.springframework.stereotype.Service;

@Service
public class PhoneNumberValidator {
    public boolean validate(String phoneNumber) {
        return phoneNumber.startsWith("55") &&
                phoneNumber.length() == 13;
    }
}
