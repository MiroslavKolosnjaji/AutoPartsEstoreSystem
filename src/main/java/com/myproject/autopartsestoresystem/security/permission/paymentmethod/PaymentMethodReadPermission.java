package com.myproject.autopartsestoresystem.security.permission.paymentmethod;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Miroslav Kološnjaji
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('paymentMethod.read')")
public @interface PaymentMethodReadPermission {
}
