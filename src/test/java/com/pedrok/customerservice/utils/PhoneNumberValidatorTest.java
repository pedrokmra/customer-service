package com.pedrok.customerservice.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PhoneNumberValidatorTest {

    @Autowired
    private PhoneNumberValidator phoneNumberValidator;

    @ParameterizedTest
    @CsvSource({
            "5551992817676, true",
            "4751992817676, false",
            "55519928176760, false"
    })
    void itShouldReturnTrueWhenValidatePhoneNumber(String phoneNumber, boolean expected) {
        // GIVEN
        boolean isValid = phoneNumberValidator.validate(phoneNumber);

        // THEN
        assertThat(isValid).isEqualTo(expected);
    }
}
