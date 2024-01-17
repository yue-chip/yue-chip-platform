package com.yue.chip.exception;

import java.io.Serializable;

/**
 * @description: 认证异常（登陆异常）
 * @author: Mr.Liu
 * @create: 2020-02-18 16:11
 */
public class AuthorizationException extends  RuntimeException implements Serializable {

    private static final long serialVersionUID = 7670720348542447805L;

    public AuthorizationException() {
    }

    public AuthorizationException(Exception e) {
        super(e);
        throw this;
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
        throw this;
    }

    public AuthorizationException(Throwable cause) {
        super(cause);
        throw this;
    }

    public AuthorizationException(String message) {
        super(message);
        throw this;
    }

    /**
     * 抛自定义异常
     * @param message
     */
    public static void throwException(final String message){
        new AuthorizationException(message);
    }
}