package dev.hieplp.library.service.impl;

import dev.hieplp.library.common.exception.user.DuplicatedEmailException;
import dev.hieplp.library.payload.request.auth.ConfirmRegisterRequest;
import dev.hieplp.library.payload.request.auth.RequestToRegisterRequest;
import dev.hieplp.library.payload.response.auth.ConfirmRegisterResponse;
import dev.hieplp.library.payload.response.auth.RequestToRegisterResponse;
import dev.hieplp.library.repository.UserRepository;
import dev.hieplp.library.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;

    @Override
    @Transactional
    public RequestToRegisterResponse requestToRegister(RequestToRegisterRequest request) {
        log.info("Request to register with request: {}", request);

        if (userRepo.existsByEmail(request.getEmail())) {
            log.error("Email {} is already in use", request.getEmail());
            throw new DuplicatedEmailException(String.format("Email %s is already in use", request.getEmail()));
        }

        if (userRepo.existsByUsername(request.getUsername())) {
            log.error("Username {} is already in use", request.getUsername());
            throw new DuplicatedEmailException(String.format("Username %s is already in use", request.getUsername()));
        }

        return null;
    }

    @Override
    public ConfirmRegisterResponse confirmRegister(ConfirmRegisterRequest request) {
        log.info("Confirm register with request: {}", request);
        return null;
    }
}
