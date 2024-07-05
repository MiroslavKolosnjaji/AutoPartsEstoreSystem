package com.myproject.autopartsestoresystem.service;


import com.myproject.autopartsestoresystem.dto.PaymentMethodDTO;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
public interface PaymentMethodService  {

     List<PaymentMethodDTO> getAll();
     PaymentMethodDTO getById(Long id);
     PaymentMethodDTO getByPaymentType(String paymentType);


}
