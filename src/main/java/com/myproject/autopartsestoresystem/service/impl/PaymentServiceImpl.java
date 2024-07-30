package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.PaymentDTO;
import com.myproject.autopartsestoresystem.exception.service.PaymentNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.PaymentProcessingException;
import com.myproject.autopartsestoresystem.mapper.PaymentMapper;
import com.myproject.autopartsestoresystem.model.Payment;
import com.myproject.autopartsestoresystem.model.PaymentStatus;
import com.myproject.autopartsestoresystem.repository.PaymentRepository;
import com.myproject.autopartsestoresystem.service.PaymentService;
import com.myproject.autopartsestoresystem.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final StripeService stripeService;

    @Override
    public PaymentDTO save(String token, PaymentDTO paymentDTO) {

        Payment payment = paymentMapper.paymentDTOToPayment(paymentDTO);
        payment.setStatus(PaymentStatus.AWAITING_PAYMENT);

        Payment savedPayment = paymentRepository.save(payment);

        try {

            stripeService.chargeCreditCard(token, savedPayment.getPurchaseOrder().getTotalAmount());
            paymentDTO.setStatus(PaymentStatus.PAID);
            return update(savedPayment.getId(), paymentDTO);

        } catch (StripeException e) {

            paymentDTO.setStatus(PaymentStatus.FAILED);
            update(savedPayment.getId(), paymentDTO);
            throw new PaymentProcessingException("Payment processing failed", e);
        }
    }

    @Override
    public PaymentDTO save(PaymentDTO entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PaymentDTO update(Long id, PaymentDTO paymentDTO) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

        payment.setPurchaseOrder(paymentDTO.getPurchaseOrder());
        payment.setCard(paymentDTO.getCard());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setStatus(PaymentStatus.AWAITING_PAYMENT);

        Payment updatedPayment = paymentRepository.save(payment);

        return paymentMapper.paymentToPaymentDTO(updatedPayment);
    }

    @Override
    public List<PaymentDTO> getAll() {
        return paymentRepository.findAll().stream().map(paymentMapper::paymentToPaymentDTO).toList();
    }

    @Override
    public PaymentDTO getById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

        return paymentMapper.paymentToPaymentDTO(payment);
    }

    @Override
    public void delete(Long id) {
        if (!paymentRepository.existsById(id))
            throw new PaymentNotFoundException("Payment not found");

        paymentRepository.deleteById(id);
    }
}
