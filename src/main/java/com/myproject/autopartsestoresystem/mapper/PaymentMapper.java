package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.PaymentDTO;
import com.myproject.autopartsestoresystem.model.Payment;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface PaymentMapper {

    PaymentDTO paymentToPaymentDTO(Payment payment);
    Payment paymentDTOToPayment(PaymentDTO paymentDTO);
}
