package dev.hieplp.library.common.exception.otp;


import dev.hieplp.library.common.exception.BaseException;

public class WrongOtpException extends BaseException {
    public WrongOtpException(String message) {
        super(message);
    }
}
