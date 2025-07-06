package com.myproject.autopartsestoresystem.payments.service;

import com.myproject.autopartsestoresystem.parts.dto.PaymentDTO;
import com.myproject.autopartsestoresystem.payments.exception.PaymentProcessingException;
import com.myproject.autopartsestoresystem.common.service.CrudService;

/**
 * @author Miroslav Kološnjaji
 */
public interface PaymentService extends CrudService<PaymentDTO, Long> {

    @Override
    PaymentDTO save(PaymentDTO paymentDTO);
    PaymentDTO save(String token, PaymentDTO entity) throws PaymentProcessingException;
}
