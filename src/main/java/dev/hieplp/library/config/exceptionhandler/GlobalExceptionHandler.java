package dev.hieplp.library.config.exceptionhandler;


import dev.hieplp.library.common.enums.response.ErrorCode;
import dev.hieplp.library.common.exception.BadRequestException;
import dev.hieplp.library.common.exception.DuplicatedException;
import dev.hieplp.library.common.exception.NotFoundException;
import dev.hieplp.library.common.exception.UnauthorizedException;
import dev.hieplp.library.common.payload.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order()
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CommonResponse> handleNotFoundException(NotFoundException e) {
        log.error("NotFoundException: {}", e.getMessage());
        var data = new CommonResponse(ErrorCode.NOT_FOUND, e.getMessage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(data);
    }

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<CommonResponse> handleDuplicatedException(DuplicatedException e) {
        log.error("DuplicatedException: {}", e.getMessage());
        var data = new CommonResponse(ErrorCode.DUPLICATED, e.getMessage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(data);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CommonResponse> handleBadRequestException(BadRequestException e) {
        log.error("BadRequestException: {}", e.getMessage());
        var data = new CommonResponse(ErrorCode.BAD_REQUEST, e.getMessage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(data);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<CommonResponse> handleUnauthorizedException(UnauthorizedException e) {
        log.error("UnauthorizedException: {}", e.getMessage());
        var data = new CommonResponse(ErrorCode.UNAUTHORIZED, e.getMessage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(data);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handleException(Exception e) {
        log.error("Exception: ", e);
        return ResponseEntity
                .internalServerError()
                .body(new CommonResponse(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
