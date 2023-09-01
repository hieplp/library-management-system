package dev.hieplp.library.common.exception.user;

import dev.hieplp.library.common.exception.BaseException;

public class InvalidUserNameOrPasswordException extends BaseException {
    public InvalidUserNameOrPasswordException() {
        super("Invalid user name or password");
    }
}
