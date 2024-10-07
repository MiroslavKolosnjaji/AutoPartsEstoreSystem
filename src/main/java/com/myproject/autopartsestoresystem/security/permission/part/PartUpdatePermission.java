package com.myproject.autopartsestoresystem.security.permission.part;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Miroslav Kološnjaji
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('part.update')")
public @interface PartUpdatePermission {
}
