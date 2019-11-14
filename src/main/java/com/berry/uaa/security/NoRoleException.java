package com.berry.uaa.security;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is thrown in case of a not activated user trying to authenticate.
 */
public class NoRoleException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public NoRoleException(String message) {
        super(message);
    }

    public NoRoleException(String message, Throwable t) {
        super(message, t);
    }
}
