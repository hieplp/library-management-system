package dev.hieplp.library.service;

import dev.hieplp.library.payload.request.auth.ConfirmRegisterRequest;
import dev.hieplp.library.payload.request.auth.RequestToRegisterRequest;
import dev.hieplp.library.payload.response.auth.ConfirmRegisterResponse;
import dev.hieplp.library.payload.response.auth.RequestToRegisterResponse;

public interface AuthService {
    RequestToRegisterResponse requestToRegister(RequestToRegisterRequest request);

    ConfirmRegisterResponse confirmRegister(ConfirmRegisterRequest request);
}
