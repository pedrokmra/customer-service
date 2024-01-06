package com.pedrok.customerservice.payment;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/payment")
@AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("{id}")
    public Payment getPayment(@PathVariable Long id) {
        return paymentService.getPayment(id);
    }

    @PostMapping
    public ResponseEntity<Void> createPayment(@Valid @RequestBody Payment payment) {
        paymentService.chargeCard(payment.getCustomerId(), payment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
