package com.mnnu.examine.common.exception;

/**
 * gwy异常
 *
 * @author qiaoh
 * @date 2021/12/09
 */
public class GwyException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String msg;

    public GwyException(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public GwyException(String message, String msg, int code) {
        super(message);
        this.msg = msg;
        this.code = code;
    }

    public GwyException(String message, Throwable cause, String msg, int code) {
        super(message, cause);
        this.msg = msg;
        this.code = code;
    }

    public GwyException(Throwable cause, String msg, int code) {
        super(cause);
        this.msg = msg;
        this.code = code;
    }

    public GwyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String msg, int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.msg = msg;
        this.code = code;
    }

    private int code = 500;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
