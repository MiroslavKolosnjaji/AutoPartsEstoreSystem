package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodDTO {

    @NotNull(groups = Update.class)
    private Long id;

    @NotNull(message = "Payment type cannot be null")
    private PaymentType paymentType;
}
