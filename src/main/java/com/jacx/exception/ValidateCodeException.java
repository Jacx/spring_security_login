package com.jacx.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * ValidateCodeException
 *
 * @author wjx
 * @date 2018/09/08
 */
@SuppressWarnings("unused")
public class ValidateCodeException extends AuthenticationException {
    public ValidateCodeException(String msg) {
        super(msg);
    }
}
