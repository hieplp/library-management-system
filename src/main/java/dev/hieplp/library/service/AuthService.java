package dev.hieplp.library.service;

import dev.hieplp.library.common.model.TokenModel;
import dev.hieplp.library.payload.request.auth.LoginRequest;
import dev.hieplp.library.payload.request.auth.RefreshAccessTokenRequest;
import dev.hieplp.library.payload.request.auth.register.ConfirmRegisterRequest;
import dev.hieplp.library.payload.request.auth.register.RequestToRegisterRequest;
import dev.hieplp.library.payload.request.auth.register.ResendRegisterOtpRequest;
import dev.hieplp.library.payload.request.auth.verify.ConfirmVerifyRequest;
import dev.hieplp.library.payload.request.auth.verify.RequestToVerifyRequest;
import dev.hieplp.library.payload.request.auth.verify.ResendVerifyOtpRequest;
import dev.hieplp.library.payload.response.auth.LoginResponse;
import dev.hieplp.library.payload.response.auth.register.ConfirmRegisterResponse;
import dev.hieplp.library.payload.response.auth.register.RequestToRegisterResponse;
import dev.hieplp.library.payload.response.auth.register.ResendRegisterOtpResponse;
import dev.hieplp.library.payload.response.auth.verify.ConfirmVerifyResponse;
import dev.hieplp.library.payload.response.auth.verify.RequestToVerifyResponse;
import dev.hieplp.library.payload.response.auth.verify.ResendVerifyOtpResponse;

public interface AuthService {
    // Register
    RequestToRegisterResponse requestToRegister(RequestToRegisterRequest request);

    ResendRegisterOtpResponse resendRegisterOtp(ResendRegisterOtpRequest request);

    ConfirmRegisterResponse confirmRegister(ConfirmRegisterRequest request);

    // Login
    LoginResponse login(LoginRequest request);

    TokenModel refreshAccessToken(RefreshAccessTokenRequest request);

    // Verify
    RequestToVerifyResponse requestToVerify(RequestToVerifyRequest request);

    ResendVerifyOtpResponse resendVerifyOtp(ResendVerifyOtpRequest request);

    ConfirmVerifyResponse confirmVerify(ConfirmVerifyRequest request);
}