package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.PaymentMethodDTO;
import com.myproject.autopartsestoresystem.model.PaymentMethod;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface PaymentMethodMapper {

    PaymentMethodDTO paymentMethodToPaymentMethodDTO(PaymentMethod paymentMethod);
    PaymentMethod paymentMethodDTOToPaymentMethod(PaymentMethodDTO paymentMethodDTO);
}
