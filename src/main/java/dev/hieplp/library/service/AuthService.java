package dev.hieplp.library.service;

import dev.hieplp.library.payload.request.auth.register.ConfirmRegisterRequest;
import dev.hieplp.library.payload.request.auth.register.RequestToRegisterRequest;
import dev.hieplp.library.payload.request.auth.register.ResendRegisterOtpRequest;
import dev.hieplp.library.payload.response.auth.register.ConfirmRegisterResponse;
import dev.hieplp.library.payload.response.auth.register.RequestToRegisterResponse;
import dev.hieplp.library.payload.response.auth.register.ResendRegisterOtpResponse;

public interface AuthService {
    RequestToRegisterResponse requestToRegister(RequestToRegisterRequest request);

    ResendRegisterOtpResponse sendRegisterOtp(ResendRegisterOtpRequest request);

    ConfirmRegisterResponse confirmRegister(ConfirmRegisterRequest request);
}
