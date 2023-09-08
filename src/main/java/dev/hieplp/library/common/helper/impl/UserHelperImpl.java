package dev.hieplp.library.common.helper.impl;

import dev.hieplp.library.common.entity.User;
import dev.hieplp.library.common.enums.user.UserStatus;
import dev.hieplp.library.common.exception.NotFoundException;
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

    @Override
    public User getActiveUser(String userId) throws NotFoundException {
        log.info("Get active user with userId: {}", userId);

        var user = userRepo.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found with userId: {}", userId);
                    return new NotFoundException("User not found");
                });

        if (!UserStatus.ACTIVE.getStatus().equals(user.getStatus())) {
            log.warn("User is not active with userId: {}", userId);
            throw new NotFoundException("User is not active");
        }

        return user;
    }
}
