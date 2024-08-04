package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.PaymentMethodDTO;
import com.myproject.autopartsestoresystem.exception.service.PaymentMethodNotFoundException;
import com.myproject.autopartsestoresystem.mapper.PaymentMethodMapper;
import com.myproject.autopartsestoresystem.model.PaymentMethod;
import com.myproject.autopartsestoresystem.model.PaymentType;
import com.myproject.autopartsestoresystem.repository.PaymentMethodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class PaymentMethodServiceImplTest {


    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private PaymentMethodMapper paymentMethodMapper;

    @InjectMocks
    private PaymentMethodServiceImpl paymentMethodService;

    private PaymentMethodDTO paymentMethodDTO;

    @BeforeEach
    void setUp() {
        paymentMethodDTO = PaymentMethodDTO.builder().paymentType(PaymentType.DEBIT_CARD).build();
    }

    @DisplayName("Get All Payment Methods")
    @Test
    void testGetAllPaymentMethods_whenListIsPopulated_thenCorrect() {

        //given
        List<PaymentMethod> paymentMethodList = List.of(mock(PaymentMethod.class), mock(PaymentMethod.class));
        when(paymentMethodRepository.findAll()).thenReturn(paymentMethodList);
        when(paymentMethodMapper.paymentMethodToPaymentMethodDTO(any(PaymentMethod.class))).thenReturn(paymentMethodDTO);

        //when
        List<PaymentMethodDTO> paymentMethodDTOList = paymentMethodService.getAll();

        assertNotNull(paymentMethodDTOList, "List of payment methods should not be null");
        assertFalse(paymentMethodDTOList.isEmpty(), "List of payment methods should not be empty");
        assertEquals(2, paymentMethodDTOList.size(), "List of payment methods should have 2 elements");

        verify(paymentMethodMapper, times(2)).paymentMethodToPaymentMethodDTO(any(PaymentMethod.class));
        verify(paymentMethodRepository).findAll();
    }

    @DisplayName("Get Payment Method By ID")
    @Test
    void testGetPaymentMethodById_whenValidIdProvided_returnPaymentMethodDTO() throws PaymentMethodNotFoundException {

        //given
        PaymentMethod paymentMethod = PaymentMethod.builder().paymentType(PaymentType.DEBIT_CARD).build();

        when(paymentMethodRepository.findById(anyLong())).thenReturn(Optional.of(paymentMethod));
        when(paymentMethodMapper.paymentMethodToPaymentMethodDTO(any(PaymentMethod.class))).thenReturn(paymentMethodDTO);

        //when
        PaymentMethodDTO foundDTO = paymentMethodService.getById(anyLong());

        assertNotNull(foundDTO, "Payment method should not be null");
        assertEquals(paymentMethod.getPaymentType(), foundDTO.getPaymentType(), "Payment type should be equal");

        verify(paymentMethodMapper).paymentMethodToPaymentMethodDTO(any(PaymentMethod.class));
        verify(paymentMethodRepository).findById(anyLong());
    }

    @DisplayName("Get Payment Method By ID Failed - Ivalid ID Provided")
    @Test
    void testGetPaymentMethodById_whenInvalidIdProvided_throwsPaymentMethodNotFoundException() {

        //given
        when(paymentMethodRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> paymentMethodService.getById(anyLong());

        //then
        assertThrows(PaymentMethodNotFoundException.class, executable, "Exception mismatch. Expected PaymentMethodNotFoundException");

        verify(paymentMethodRepository).findById(anyLong());
    }

    @DisplayName("Get Payment Method By Payment Type")
    @Test
    void testGetPaymentMethodByPaymentType_whenValidPaymentTypeProvided_returnsPaymentMethodDTO() throws PaymentMethodNotFoundException {

        //given
        PaymentMethod paymentMethod = PaymentMethod.builder().paymentType(PaymentType.DEBIT_CARD).build();
        when(paymentMethodRepository.findPaymentMethodByPaymentType(anyString())).thenReturn(Optional.of(paymentMethod));
        when(paymentMethodMapper.paymentMethodToPaymentMethodDTO(any(PaymentMethod.class))).thenReturn(paymentMethodDTO);

        //when
        PaymentMethodDTO foundDTO = paymentMethodService.getByPaymentType(anyString());


        assertNotNull(foundDTO, "Payment method should not be null");

        verify(paymentMethodMapper).paymentMethodToPaymentMethodDTO(any(PaymentMethod.class));
        verify(paymentMethodRepository).findPaymentMethodByPaymentType(anyString());
    }

    @DisplayName("Get Payment By Payment Type Failed - Invalid Payment Type Provided")
    @Test
    void testGetPaymentMethodByPaymentType_whenInvalidPaymentTypeProvided_throwsPaymentMethodNotFoundException() {

        //given
        when(paymentMethodRepository.findPaymentMethodByPaymentType(anyString())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> paymentMethodService.getByPaymentType("Invalid Type");

        //then
        assertThrows(PaymentMethodNotFoundException.class, executable, "Exception mismatch. Expected PaymentMethodNotFoundException");

        verify(paymentMethodRepository).findPaymentMethodByPaymentType(anyString());
    }
}