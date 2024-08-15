package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Miroslav Kološnjaji
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
