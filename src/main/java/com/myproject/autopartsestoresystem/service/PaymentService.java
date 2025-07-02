package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.parts.dto.PaymentDTO;
import com.myproject.autopartsestoresystem.exception.service.PaymentProcessingException;

/**
 * @author Miroslav Kološnjaji
 */
public interface PaymentService extends CrudService<PaymentDTO, Long>{

    @Override
    PaymentDTO save(PaymentDTO paymentDTO);
    PaymentDTO save(String token, PaymentDTO entity) throws PaymentProcessingException;
}
