package dev.hieplp.library.common.helper;

import dev.hieplp.library.common.entity.User;
import dev.hieplp.library.common.exception.NotFoundException;

public interface UserHelper {
    void validateUsername(String username);

    void validateEmail(String email);

    User getUser(String userId) throws NotFoundException;

    User getActiveUser(String userId) throws NotFoundException;
}
