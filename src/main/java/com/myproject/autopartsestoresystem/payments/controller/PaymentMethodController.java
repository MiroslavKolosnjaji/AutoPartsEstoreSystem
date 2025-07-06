package com.myproject.autopartsestoresystem.payments.controller;

import com.myproject.autopartsestoresystem.payments.dto.PaymentMethodDTO;
import com.myproject.autopartsestoresystem.common.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.security.permission.paymentmethod.PaymentMethodReadPermission;
import com.myproject.autopartsestoresystem.payments.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@RestController
@RequestMapping("/api/paymentMethod")
@RequiredArgsConstructor
public class PaymentMethodController {

    public static final String PAYMENT_METHOD_URI = "/api/paymentMethod";
    public static final String PAYMENT_METHOD_ID = "/{paymentMethodId}";
    public static final String PAYMENT_METHOD_URI_WITH_ID = PAYMENT_METHOD_URI + PAYMENT_METHOD_ID;

    private final PaymentMethodService paymentMethodService;

    @PaymentMethodReadPermission
    @GetMapping()
    public ResponseEntity<List<PaymentMethodDTO>> getAllPaymentMethods() {

        List<PaymentMethodDTO> paymentMethods = paymentMethodService.getAll();

        if (paymentMethods.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(paymentMethods);
    }

    @PaymentMethodReadPermission
    @GetMapping(PAYMENT_METHOD_ID)
    public ResponseEntity<PaymentMethodDTO> getPaymentMethodById(@PathVariable("paymentMethodId") Long paymentMethodId) throws EntityNotFoundException {

        PaymentMethodDTO paymentMethodDTO = paymentMethodService.getById(paymentMethodId);
        return new ResponseEntity<>(paymentMethodDTO, HttpStatus.OK);

    }

    @PaymentMethodReadPermission
    @GetMapping("/payment_type")
    public ResponseEntity<PaymentMethodDTO> getPaymentMethodType(@RequestParam("payment_type") String paymentType) throws EntityNotFoundException {

        PaymentMethodDTO paymentMethodDTO = paymentMethodService.getByPaymentType(paymentType);
        return new ResponseEntity<>(paymentMethodDTO, HttpStatus.OK);

    }


}
