package dev.hieplp.library.config.exceptionhandler;

import dev.hieplp.library.common.enums.response.ErrorCode;
import dev.hieplp.library.common.exception.user.DuplicatedEmailException;
import dev.hieplp.library.common.exception.user.DuplicatedUsernameException;
import dev.hieplp.library.common.exception.user.InvalidUserNameOrPasswordException;
import dev.hieplp.library.common.exception.user.NotVerifiedException;
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
public class UserExceptionHandler {
    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<CommonResponse> handleDuplicatedEmailException(DuplicatedEmailException e) {
        log.debug("DuplicatedEmailException: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonResponse(ErrorCode.USER_DUPLICATED_EMAIL));
    }

    @ExceptionHandler(DuplicatedUsernameException.class)
    public ResponseEntity<CommonResponse> handleDuplicatedUsernameException(DuplicatedUsernameException e) {
        log.debug("DuplicatedUsernameException: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonResponse(ErrorCode.USER_DUPLICATE_USERNAME));
    }

    @ExceptionHandler(InvalidUserNameOrPasswordException.class)
    public ResponseEntity<CommonResponse> handlePasswordNotMatchException(InvalidUserNameOrPasswordException e) {
        log.debug("PasswordNotMatchException: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonResponse(ErrorCode.USER_INVALID_PASSWORD_OR_USERNAME));
    }

    @ExceptionHandler(NotVerifiedException.class)
    public ResponseEntity<CommonResponse> handleNotVerifiedException(NotVerifiedException e) {
        log.debug("NotVerifiedException: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonResponse(ErrorCode.USER_NOT_VERIFIED));
    }
}
