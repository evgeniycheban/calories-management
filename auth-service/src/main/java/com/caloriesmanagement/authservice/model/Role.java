package com.caloriesmanagement.authservice.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * User role enum.
 *
 * @author Evgeniy Cheban
 */
public enum Role implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_USER;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthority() {
        return name();
    }
}
