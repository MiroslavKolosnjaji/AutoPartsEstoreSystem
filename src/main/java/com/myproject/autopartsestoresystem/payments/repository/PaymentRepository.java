package com.myproject.autopartsestoresystem.payments.repository;

import com.myproject.autopartsestoresystem.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Miroslav Kološnjaji
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
