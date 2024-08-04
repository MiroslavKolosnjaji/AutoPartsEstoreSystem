package com.myproject.autopartsestoresystem.service;


import com.myproject.autopartsestoresystem.dto.PaymentMethodDTO;
import com.myproject.autopartsestoresystem.exception.service.PaymentMethodNotFoundException;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
public interface PaymentMethodService  {

     List<PaymentMethodDTO> getAll();
     PaymentMethodDTO getById(Long id) throws PaymentMethodNotFoundException;
     PaymentMethodDTO getByPaymentType(String paymentType) throws PaymentMethodNotFoundException;


}
