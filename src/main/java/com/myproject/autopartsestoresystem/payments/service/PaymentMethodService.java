package com.myproject.autopartsestoresystem.payments.service;


import com.myproject.autopartsestoresystem.payments.dto.PaymentMethodDTO;
import com.myproject.autopartsestoresystem.payments.exception.PaymentMethodNotFoundException;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
public interface PaymentMethodService  {

     List<PaymentMethodDTO> getAll();
     PaymentMethodDTO getById(Long id) throws PaymentMethodNotFoundException;
     PaymentMethodDTO getByPaymentType(String paymentType) throws PaymentMethodNotFoundException;


}
