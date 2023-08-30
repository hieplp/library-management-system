package dev.hieplp.library.common.exception.user;


import dev.hieplp.library.common.exception.BaseException;

public class DuplicatedEmailException extends BaseException {
    public DuplicatedEmailException(String message) {
        super(message);
    }
}
