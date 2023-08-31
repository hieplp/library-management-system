package dev.hieplp.library.common.exception.otp;


import dev.hieplp.library.common.exception.BaseException;

public class ExceededOtpQuotaException extends BaseException {
    public ExceededOtpQuotaException(String message) {
        super(message);
    }
}
