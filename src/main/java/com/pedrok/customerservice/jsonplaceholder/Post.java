package com.pedrok.customerservice.jsonplaceholder;

public record Post(
        Long userId,
        Long id,
        String title,
        String body
) {
}
