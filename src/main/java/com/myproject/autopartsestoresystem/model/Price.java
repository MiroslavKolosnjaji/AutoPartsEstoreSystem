package com.myproject.autopartsestoresystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "price")
public class Price {

    @EmbeddedId
    private PriceId id;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @LastModifiedDate
    @Column(name = "date_modified")
    private LocalDateTime dateModified;

}
