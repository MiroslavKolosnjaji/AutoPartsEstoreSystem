package com.myproject.autopartsestoresystem.security.permission.store;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Miroslav Kološnjaji
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('store.update')")
public @interface StoreUpdatePermission {
}
