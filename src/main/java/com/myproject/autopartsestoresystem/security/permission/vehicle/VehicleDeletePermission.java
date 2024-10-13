package com.myproject.autopartsestoresystem.security.permission.vehicle;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Miroslav Kološnjaji
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('vehicle.delete')")
public @interface VehicleDeletePermission {
}
