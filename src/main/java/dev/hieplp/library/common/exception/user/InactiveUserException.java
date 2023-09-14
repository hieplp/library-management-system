package dev.hieplp.library.common.exception.user;

import dev.hieplp.library.common.exception.BaseException;

public class InactiveUserException extends BaseException {
    public InactiveUserException(String message) {
        super(message);
    }
}
