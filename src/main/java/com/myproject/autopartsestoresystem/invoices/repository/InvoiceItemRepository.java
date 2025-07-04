package com.myproject.autopartsestoresystem.invoices.repository;

import com.myproject.autopartsestoresystem.invoices.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Miroslav Kološnjaji
 */
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {
}
