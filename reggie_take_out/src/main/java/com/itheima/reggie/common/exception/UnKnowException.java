package com.itheima.reggie.common.exception;

/**
 *  未知异常
 */
public class UnKnowException extends Exception{

    public UnKnowException() {
    }

    public UnKnowException(String message) {
        super(message);
    }

    public UnKnowException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnKnowException(Throwable cause) {
        super(cause);
    }

    public UnKnowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
