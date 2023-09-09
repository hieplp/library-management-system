package dev.hieplp.library.service;

import dev.hieplp.library.common.model.TokenModel;
import dev.hieplp.library.payload.request.auth.LoginRequest;
import dev.hieplp.library.payload.request.auth.RefreshAccessTokenRequest;
import dev.hieplp.library.payload.request.auth.register.ConfirmRegisterRequest;
import dev.hieplp.library.payload.request.auth.register.RequestToRegisterRequest;
import dev.hieplp.library.payload.request.auth.register.ResendRegisterOtpRequest;
import dev.hieplp.library.payload.response.auth.LoginResponse;
import dev.hieplp.library.payload.response.auth.register.ConfirmRegisterResponse;
import dev.hieplp.library.payload.response.auth.register.RequestToRegisterResponse;
import dev.hieplp.library.payload.response.auth.register.ResendRegisterOtpResponse;

public interface AuthService {
    RequestToRegisterResponse requestToRegister(RequestToRegisterRequest request);

    ResendRegisterOtpResponse resendRegisterOtp(ResendRegisterOtpRequest request);

    ConfirmRegisterResponse confirmRegister(ConfirmRegisterRequest request);

    LoginResponse login(LoginRequest request);

    TokenModel refreshAccessToken(RefreshAccessTokenRequest request);
}