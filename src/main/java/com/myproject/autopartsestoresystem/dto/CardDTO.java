package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.Customer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.sql.Update;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDateTime;

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

    @NotBlank(message = "Card number cannot be blank")
    @CreditCardNumber(message = "Invalid credit card number format")
    private String cardNumber;

    @NotBlank(message = "Card holder name cannot be blank")
    @Size(max = 125, message = "Card holder name cannot be longer than 125 characters")
    private String cardHolder;

    @NotBlank(message = "Expiry date cannot be blank")
    private LocalDateTime expiryDate;

    @NotBlank(message = "CVV cannot be blank")
    @Pattern(regexp = "\\d{3,4}", message = "CVV must be numeric and max length is 3 or 4 depends of your card")
    private String cvv;

    @NotNull(message = "Customer cannot be null")
    private Customer customer;
}
