package dev.hieplp.library.common.exception;

import java.io.Serial;

public class BaseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5070192011710943669L;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }
}