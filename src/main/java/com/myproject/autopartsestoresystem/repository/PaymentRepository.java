package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Miroslav Kološnjaji
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
