package com.pedrok.customerservice.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.List;

public record ApiException(
        List<String> message,
        @JsonIgnore
        Throwable throwable,
        HttpStatus httpStatus,
        ZonedDateTime zonedDateTime
) {
}
