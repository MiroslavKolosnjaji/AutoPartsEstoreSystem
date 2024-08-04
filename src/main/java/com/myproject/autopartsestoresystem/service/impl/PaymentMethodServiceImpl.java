package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.PaymentMethodDTO;
import com.myproject.autopartsestoresystem.exception.service.PaymentMethodNotFoundException;
import com.myproject.autopartsestoresystem.mapper.PaymentMethodMapper;
import com.myproject.autopartsestoresystem.repository.PaymentMethodRepository;
import com.myproject.autopartsestoresystem.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;


    @Override
    public List<PaymentMethodDTO> getAll() {
        return paymentMethodRepository.findAll().stream()
                .map(paymentMethodMapper::paymentMethodToPaymentMethodDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentMethodDTO getById(Long id) throws PaymentMethodNotFoundException {
        return paymentMethodRepository.findById(id)
                .map(paymentMethodMapper::paymentMethodToPaymentMethodDTO)
                .orElseThrow(() -> new PaymentMethodNotFoundException("Payment method not found"));
    }

    @Override
    public PaymentMethodDTO getByPaymentType(String paymentType) throws PaymentMethodNotFoundException {
        return paymentMethodRepository.findPaymentMethodByPaymentType(paymentType)
                .map(paymentMethodMapper::paymentMethodToPaymentMethodDTO)
                .orElseThrow(() -> new PaymentMethodNotFoundException("Payment method not found"));
    }


}
