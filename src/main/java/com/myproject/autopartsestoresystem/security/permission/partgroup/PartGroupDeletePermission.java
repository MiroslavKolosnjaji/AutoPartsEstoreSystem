package com.myproject.autopartsestoresystem.security.permission.partgroup;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Miroslav Kološnjaji
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('partgroup.delete')")
public @interface PartGroupDeletePermission {
}
