package com.myproject.autopartsestoresystem.payments.mapper;

import com.myproject.autopartsestoresystem.payments.dto.PaymentMethodDTO;
import com.myproject.autopartsestoresystem.payments.entity.PaymentMethod;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface PaymentMethodMapper {

    PaymentMethodDTO paymentMethodToPaymentMethodDTO(PaymentMethod paymentMethod);
    PaymentMethod paymentMethodDTOToPaymentMethod(PaymentMethodDTO paymentMethodDTO);
}
