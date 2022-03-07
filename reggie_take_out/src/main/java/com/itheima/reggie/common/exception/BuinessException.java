package com.itheima.reggie.common.exception;

/**
 *  业务异常
 */
public class BuinessException extends RuntimeException {

    public BuinessException() {
    }

    public BuinessException(String message) {
        super(message);
    }

    public BuinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuinessException(Throwable cause) {
        super(cause);
    }

    public BuinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
