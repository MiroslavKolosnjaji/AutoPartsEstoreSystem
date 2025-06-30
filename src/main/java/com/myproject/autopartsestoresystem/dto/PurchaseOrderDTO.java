package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.customers.dto.CustomerDTO;
import com.myproject.autopartsestoresystem.model.PaymentType;
import com.myproject.autopartsestoresystem.model.PurchaseOrderItem;
import com.myproject.autopartsestoresystem.model.PurchaseOrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderDTO {

    @Transient
    private String paymentToken;

    @NotNull(groups = Update.class, message = "Id cannot be null")
    private Long id;

    @NotNull(groups = Update.class, message = "Purchase order number cannot be null")
    private UUID purchaseOrderNumber;

    @NotNull(groups = Update.class, message = "Status cannot be null")
    private PurchaseOrderStatus status;

    @NotNull(groups = Update.class, message = "Total amount cannot be null")
    @Positive(message = "Total amount cannot be negative")
    private BigDecimal totalAmount;

    @NotNull(message = "Item list cannot be null")
    private List<PurchaseOrderItem> items;

    private CustomerDTO customer;

    private PaymentType paymentType;
}
