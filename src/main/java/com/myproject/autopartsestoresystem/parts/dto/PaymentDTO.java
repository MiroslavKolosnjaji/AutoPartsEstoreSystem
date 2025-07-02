package com.myproject.autopartsestoresystem.parts.dto;

import com.myproject.autopartsestoresystem.model.Card;
import com.myproject.autopartsestoresystem.model.PaymentMethod;
import com.myproject.autopartsestoresystem.model.PaymentStatus;
import com.myproject.autopartsestoresystem.model.PurchaseOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

import java.math.BigDecimal;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {

    @NotNull(groups = Update.class)
    private Long id;

    @Positive(message = "Amount cannot be negative")
    private BigDecimal amount;

    @NotNull(message = "Payment status cannot be null")
    private PaymentStatus status;

    @NotNull(message = "Credit card cannot be null")
    private Card card;

    @NotNull(message = "Purchase Order cannot be null")
    private PurchaseOrder purchaseOrder;

    @NotNull(message = "Payment method cannot be null")
    private PaymentMethod paymentMethod;
}
