package dev.hieplp.library.common.exception.user;


import dev.hieplp.library.common.exception.BaseException;

public class DuplicatedUsernameException extends BaseException {
    public DuplicatedUsernameException(String message) {
        super(message);
    }
}
