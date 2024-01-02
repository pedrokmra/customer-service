package com.pedrok.customerservice.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name must not be empty")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "password must not be empty")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "email must not be empty")
    @Email(message = "email must be well-formatted")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "phoneNumber must not be empty")
    @Column(nullable = false, unique = true)
    private String phoneNumber;
}
