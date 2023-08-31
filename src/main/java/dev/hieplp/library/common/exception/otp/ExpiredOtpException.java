package dev.hieplp.library.common.exception.otp;


import dev.hieplp.library.common.exception.BaseException;

public class ExpiredOtpException extends BaseException {
    public ExpiredOtpException(String msg) {
        super(msg);
    }
}
