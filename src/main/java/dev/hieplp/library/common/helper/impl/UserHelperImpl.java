package dev.hieplp.library.common.helper.impl;

import dev.hieplp.library.common.exception.user.DuplicatedEmailException;
import dev.hieplp.library.common.helper.UserHelper;
import dev.hieplp.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHelperImpl implements UserHelper {

    private final UserRepository userRepo;

    @Override
    public void validateUsername(String username) {
        if (userRepo.existsByUsername(username)) {
            log.warn("Username {} is already in use", username);
            throw new DuplicatedEmailException(String.format("Username %s is already in use", username));
        }
    }

    @Override
    public void validateEmail(String email) {
        log.info("Checking if email {} is already in use", email);
        if (userRepo.existsByEmail(email)) {
            log.warn("Email {} is already in use", email);
            throw new DuplicatedEmailException(String.format("Email %s is already in use", email));
        }
    }
}
