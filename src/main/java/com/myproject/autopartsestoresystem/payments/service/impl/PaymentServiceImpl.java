package com.myproject.autopartsestoresystem.payments.service.impl;

import com.myproject.autopartsestoresystem.parts.dto.PaymentDTO;
import com.myproject.autopartsestoresystem.payments.exception.PaymentNotFoundException;
import com.myproject.autopartsestoresystem.payments.exception.PaymentProcessingException;
import com.myproject.autopartsestoresystem.payments.mapper.PaymentMapper;
import com.myproject.autopartsestoresystem.payments.entity.Payment;
import com.myproject.autopartsestoresystem.payments.entity.PaymentStatus;
import com.myproject.autopartsestoresystem.payments.repository.PaymentRepository;
import com.myproject.autopartsestoresystem.payments.service.PaymentService;
import com.myproject.autopartsestoresystem.payments.service.StripeService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public PaymentDTO save(String token, PaymentDTO paymentDTO) throws PaymentProcessingException {

        PaymentDTO savedPayment = save(paymentDTO);

        if (token == null)
            return savedPayment;

        try {

            stripeService.chargeCreditCard(token, savedPayment.getPurchaseOrder().getTotalAmount());
            paymentDTO.setStatus(PaymentStatus.PAID);
            return update(savedPayment.getId(), paymentDTO);

        } catch (StripeException | PaymentNotFoundException e) {

            paymentDTO.setStatus(PaymentStatus.FAILED);

            try {
                update(savedPayment.getId(), paymentDTO);
            } catch (PaymentNotFoundException e1) {
                throw new PaymentProcessingException("Payment processing failed", e1);
            }
            throw new PaymentProcessingException("Payment processing failed", e);

        }
    }

    @Override
    public PaymentDTO save(PaymentDTO paymentDTO){
        Payment payment = paymentMapper.paymentDTOToPayment(paymentDTO);
        payment.setStatus(PaymentStatus.AWAITING_PAYMENT);

        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.paymentToPaymentDTO(savedPayment);
    }

    @Override
    public PaymentDTO update(Long id, PaymentDTO paymentDTO) throws PaymentNotFoundException {

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
    public PaymentDTO getById(Long id) throws PaymentNotFoundException {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

        return paymentMapper.paymentToPaymentDTO(payment);
    }

    @Override
    public void delete(Long id) throws PaymentNotFoundException {
        if (!paymentRepository.existsById(id))
            throw new PaymentNotFoundException("Payment not found");

        paymentRepository.deleteById(id);
    }
}
