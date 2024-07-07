package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.Customer;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.sql.Update;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.time.LocalDate;


/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDTO {

    @NotNull(groups = Update.class)
    private Long id;

    @CreditCardNumber(message = "Invalid credit card number")
    private String cardNumber;

    @NotBlank(message = "Card holder name cannot be empty")
    @Size(max = 125, message = "Card holder name cannot be longer than 125 characters")
    private String cardHolder;

    @NotNull(message = "Expiry date cannot be null")
    private LocalDate expiryDate;

    @Pattern(regexp = "\\d{3,4}", message = "Invalid CVV")
    private String cvv;

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;
}
