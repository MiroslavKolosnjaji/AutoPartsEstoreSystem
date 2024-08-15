package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Miroslav Kološnjaji
 */
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {
}
