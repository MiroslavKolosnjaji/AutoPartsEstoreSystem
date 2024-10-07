package com.myproject.autopartsestoresystem.security.permission.brand;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Miroslav Kološnjaji
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('brand.read')")
public @interface BrandReadPermission {
}
