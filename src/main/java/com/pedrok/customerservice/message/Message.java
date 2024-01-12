package com.pedrok.customerservice.message;

import jakarta.validation.constraints.NotBlank;
public record Message(
        @NotBlank(message = "to must not be empty")
        String to,
        @NotBlank(message = "message must not be empty")
        String message
) {
}
