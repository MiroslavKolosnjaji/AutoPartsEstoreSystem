package com.myproject.autopartsestoresystem.payments.repository;

import com.myproject.autopartsestoresystem.payments.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    Optional<PaymentMethod> findPaymentMethodByPaymentType(String paymentType);
}
