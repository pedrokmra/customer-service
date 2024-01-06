package com.pedrok.customerservice.payment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "customerId must not be empty")
    @Column(nullable = false)
    private Long customerId;

    @NotNull(message = "amount must not be null")
    @Positive(message = "amount must be positive")
    @Column(nullable = false)
    private BigDecimal amount;

    @NotNull(message = "currency must not be empty")
    @Column(nullable = false)
    private Currency currency;

    @NotBlank(message = "source must not be empty")
    @Column(nullable = false)
    private String source;

    private String description;
}
