package com.myproject.autopartsestoresystem.security.permission.city;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Miroslav Kološnjaji
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('city.update')")
public @interface CityUpdatePermission {
}
