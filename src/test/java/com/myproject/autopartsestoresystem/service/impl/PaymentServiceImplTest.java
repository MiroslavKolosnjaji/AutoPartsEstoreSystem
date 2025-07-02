package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.parts.dto.PaymentDTO;
import com.myproject.autopartsestoresystem.exception.service.PaymentNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.PaymentProcessingException;
import com.myproject.autopartsestoresystem.mapper.PaymentMapper;
import com.myproject.autopartsestoresystem.model.*;
import com.myproject.autopartsestoresystem.repository.PaymentRepository;
import com.myproject.autopartsestoresystem.service.StripeService;
import com.stripe.exception.CardException;
import com.stripe.model.Charge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private StripeService stripeService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentDTO paymentDTO;

    private Payment payment;

    @BeforeEach
    void setUp() {
        paymentDTO = PaymentDTO.builder()
                .id(1L)
                .status(PaymentStatus.AWAITING_PAYMENT)
                .paymentMethod(PaymentMethod.builder().id(1L).paymentType(PaymentType.CREDIT_CARD).build())
                .card(Card.builder().build())
                .amount(new BigDecimal("120.00"))
                .purchaseOrder(PurchaseOrder.builder().totalAmount(new BigDecimal("120.00")).build())
                .build();

        payment = Payment.builder()
                .id(1L)
                .status(PaymentStatus.AWAITING_PAYMENT)
                .paymentMethod(PaymentMethod.builder().id(1L).paymentType(PaymentType.CREDIT_CARD).build())
                .card(Card.builder().build())
                .amount(new BigDecimal("120.00"))
                .purchaseOrder(PurchaseOrder.builder().totalAmount(new BigDecimal("120.00")).build())
                .build();
    }

    @DisplayName("Save Payment")
    @Test
    void testSavePayment_whenValidDetailsProvided_returnsPaymentDTO() throws Exception {

        //given
        Charge charge = mock(Charge.class);
        String token = "tok_visa";

        when(paymentMapper.paymentDTOToPayment(paymentDTO)).thenReturn(payment);
        when(paymentMapper.paymentToPaymentDTO(payment)).thenReturn(paymentDTO);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(stripeService.chargeCreditCard(token, paymentDTO.getAmount())).thenReturn(charge);
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));

        //when
        PaymentDTO savedDTO = paymentService.save(token, paymentDTO);

        //then
        assertNotNull(savedDTO);
        assertAll("PaymentDTO field validation",
                () -> assertEquals(paymentDTO.getStatus(), savedDTO.getStatus(), "PaymentDTO status mismatch"),
                () -> assertEquals(paymentDTO.getPaymentMethod(), savedDTO.getPaymentMethod(), "PaymentDTO payment method mismatch"),
                () -> assertEquals(paymentDTO.getCard(), savedDTO.getCard(), "PaymentDTO card mismatch"),
                () -> assertEquals(paymentDTO.getAmount(), savedDTO.getAmount(), "PaymentDTO amount mismatch"),
                () -> assertEquals(paymentDTO.getPurchaseOrder(), savedDTO.getPurchaseOrder(), "PaymentDTO purchase order mismatch"));

        verify(paymentMapper).paymentDTOToPayment(paymentDTO);
        verify(paymentMapper, times(2)).paymentToPaymentDTO(payment);
        verify(stripeService).chargeCreditCard(token, paymentDTO.getAmount());
        verify(paymentRepository, times(2)).save(payment);
        verify(paymentRepository).findById(anyLong());
    }

    @DisplayName("Save Payment Failed - Credit Card Denied")
    @Test
    void testSavePayment_whenChargingCreditCardIsDenied_throwsPaymentProcessingException() throws Exception {


        //given
        String token = "tok_visa";

        when(paymentMapper.paymentDTOToPayment(paymentDTO)).thenReturn(payment);
        when(paymentMapper.paymentToPaymentDTO(payment)).thenReturn(paymentDTO);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(stripeService.chargeCreditCard(token, paymentDTO.getAmount())).thenThrow(CardException.class);
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));

        //when
        Executable executable = () -> paymentService.save(token, paymentDTO);

        //then
        assertThrows(PaymentProcessingException.class, executable, "Exception mismatch. PaymentProcessingException expected");

        verify(paymentMapper).paymentDTOToPayment(paymentDTO);
        verify(paymentMapper, times(2)).paymentToPaymentDTO(payment);
        verify(stripeService).chargeCreditCard(token, paymentDTO.getAmount());
        verify(paymentRepository, times(2)).save(payment);
        verify(paymentRepository).findById(anyLong());
    }

    @DisplayName("Update Payment")
    @Test
    void testUpdatePayment_whenValidDetailsProvided_returnsPaymentDTO() throws PaymentNotFoundException {

        //given

        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(paymentMapper.paymentToPaymentDTO(payment)).thenReturn(paymentDTO);

        when(paymentRepository.save(payment)).thenReturn(payment);

        //when
        PaymentDTO updatedDTO = paymentService.update(anyLong(), paymentDTO);

        //then
        assertNotNull(updatedDTO);
        assertEquals(paymentDTO, updatedDTO, "Updated Payment DTO should be equal to Payment DTO");

        verify(paymentRepository).findById(anyLong());
        verify(paymentMapper).paymentToPaymentDTO(payment);
        verify(paymentRepository).save(payment);

    }

    @DisplayName("Update Payment Failed - Invalid ID Provided")
    @Test
    void testUpdatePayment_whenPaymentNotFound_throwsPaymentNotFoundException() {

        //given
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> paymentService.update(anyLong(), paymentDTO);

        //then
        assertThrows(PaymentNotFoundException.class, executable, "Exception mismatch. PaymentNotFoundException expected");
        verify(paymentRepository).findById(anyLong());
    }

    @DisplayName("Get All Payments")
    @Test
    void testGetAllPayments_whenListIsPopulated_returnsListOfPaymentDTO() {

        //given
        List<Payment> list = List.of(mock(Payment.class), mock(Payment.class));

        when(paymentRepository.findAll()).thenReturn(list);

        //when
        List<PaymentDTO> getAll = paymentService.getAll();

        //then
        assertNotNull(getAll, "List should not be null");
        assertEquals(list.size(), getAll.size(), "List size mismatch");

        verify(paymentRepository).findAll();
    }

    @DisplayName("Get Payment By ID")
    @Test
    void testGetByID_whenValidIDProvided_returnsPaymentDTO() throws PaymentNotFoundException {

        //given
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(paymentMapper.paymentToPaymentDTO(payment)).thenReturn(paymentDTO);

        //when
        PaymentDTO foundDTO = paymentService.getById(anyLong());

        //then
        assertNotNull(foundDTO, "Found Payment DTO should not be null");
        assertEquals(paymentDTO, foundDTO, "Found DTO should be equal to Payment DTO");

        verify(paymentRepository).findById(anyLong());
        verify(paymentMapper).paymentToPaymentDTO(payment);
    }

    @DisplayName("Get Payment By ID Failed - Invalid ID Provided")
    @Test
    void testGetByID_whenInvalidIDProvided_throwsPaymentNotFoundException() {

        //given
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> paymentService.getById(anyLong());

        //then
        assertThrows(PaymentNotFoundException.class, executable, "Exception mismatch. PaymentNotFoundException expected");
    }

    @DisplayName("Delete Payment")
    @Test
    void testDeletePaymentByID_whenValidIDProvided_thenCorrect() throws PaymentNotFoundException {

        //given
        when(paymentRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(paymentRepository).deleteById(anyLong());

        //when
        paymentService.delete(anyLong());

        //then
        verify(paymentRepository).deleteById(anyLong());
        verify(paymentRepository).deleteById(anyLong());
    }

    @DisplayName("Delete Payment Failed - Invalid ID Provided")
    @Test
    void testDeletePaymentByID_whenInvalidIDProvided_throwsPaymentNotFoundException() {

        //given
        when(paymentRepository.existsById(anyLong())).thenReturn(false);

        //when
        Executable executable = () -> paymentService.delete(anyLong());

        //then
        assertThrows(PaymentNotFoundException.class, executable, "Exception mismatch. PaymentNotFoundException expected");
    }
}