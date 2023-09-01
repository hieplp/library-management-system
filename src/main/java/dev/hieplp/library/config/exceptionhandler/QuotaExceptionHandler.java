package dev.hieplp.library.config.exceptionhandler;

import dev.hieplp.library.common.enums.response.ErrorCode;
import dev.hieplp.library.common.exception.otp.ExceededOtpQuotaException;
import dev.hieplp.library.common.exception.otp.ExpiredOtpException;
import dev.hieplp.library.common.exception.otp.IssuedOtpException;
import dev.hieplp.library.common.exception.otp.WrongOtpException;
import dev.hieplp.library.common.payload.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class QuotaExceptionHandler {
    @ExceptionHandler(ExceededOtpQuotaException.class)
    public ResponseEntity<CommonResponse> handleExceededOtpQuotaException(ExceededOtpQuotaException e) {
        log.debug("ExceededOtpQuotaException: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonResponse(ErrorCode.OTP_QUOTA_EXCEEDED));
    }

    @ExceptionHandler(ExpiredOtpException.class)
    public ResponseEntity<CommonResponse> handleExpiredOtpException(ExpiredOtpException e) {
        log.debug("ExpiredOtpException: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonResponse(ErrorCode.OTP_EXPIRED));
    }

    @ExceptionHandler(IssuedOtpException.class)
    public ResponseEntity<CommonResponse> handleIssuedOtpException(IssuedOtpException e) {
        log.debug("IssuedOtpException: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonResponse(ErrorCode.OTP_IS_USED));
    }

    @ExceptionHandler(WrongOtpException.class)
    public ResponseEntity<CommonResponse> handleWrongOtpException(WrongOtpException e) {
        log.debug("WrongOtpException: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonResponse(ErrorCode.OTP_WRONG));
    }
}
