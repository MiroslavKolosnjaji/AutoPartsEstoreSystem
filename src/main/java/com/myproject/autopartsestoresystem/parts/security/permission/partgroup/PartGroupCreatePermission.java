package com.myproject.autopartsestoresystem.parts.security.permission.partgroup;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Miroslav Kološnjaji
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('partGroup.create')")
public @interface PartGroupCreatePermission {
}
