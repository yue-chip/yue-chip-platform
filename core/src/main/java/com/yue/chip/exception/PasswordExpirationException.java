package com.yue.chip.exception;

import java.io.Serializable;

public class PasswordExpirationException extends  RuntimeException implements Serializable {

    private static final long serialVersionUID = 7670720348542447804L;

    public PasswordExpirationException() {
    }

    public PasswordExpirationException(Exception e) {
        super(e);
    }

    public PasswordExpirationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordExpirationException(Throwable cause) {
        super(cause);
    }

    public PasswordExpirationException(String message) {
        super(message);
    }

    /**
     * 抛自定义异常
     * @param message
     */
    public static void throwException(String message){
        throw new PasswordExpirationException(message);
    }
}
