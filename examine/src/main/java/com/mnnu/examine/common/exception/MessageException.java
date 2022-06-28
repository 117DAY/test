package com.mnnu.examine.common.exception;

/**
 * 信息异常
 *
 * @author qiaoh
 * @date 2021/12/07
 */
public class MessageException extends RuntimeException {
    public MessageException() {
        super();
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }

    protected MessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
