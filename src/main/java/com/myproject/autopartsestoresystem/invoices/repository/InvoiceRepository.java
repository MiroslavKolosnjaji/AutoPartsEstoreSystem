package com.myproject.autopartsestoresystem.invoices.repository;

import com.myproject.autopartsestoresystem.invoices.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Miroslav Kološnjaji
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
