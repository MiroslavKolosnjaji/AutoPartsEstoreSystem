package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.CartStatus;
import com.myproject.autopartsestoresystem.model.Customer;
import com.myproject.autopartsestoresystem.model.Item;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

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
public class CartDTO {

    @NotNull(groups = Update.class)
    private Long id;

    @NotNull(message = "Cart number cannot be null")
    private UUID cartNumber;

    @NotNull(message = "Status cannot be null")
    private CartStatus status;

    @NotNull(message = "Total amount cannot be null")
    @Positive
    private BigDecimal totalAmount;

    @NotNull(message = "Item list cannot be null")
    private List<ItemDTO> items;

    private CustomerDTO customer;
}
