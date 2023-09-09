package dev.hieplp.library.controller;

import dev.hieplp.library.common.enums.response.SuccessCode;
import dev.hieplp.library.common.payload.response.CommonResponse;
import dev.hieplp.library.payload.request.auth.LoginRequest;
import dev.hieplp.library.payload.request.auth.RefreshAccessTokenRequest;
import dev.hieplp.library.payload.request.auth.register.ConfirmRegisterRequest;
import dev.hieplp.library.payload.request.auth.register.RequestToRegisterRequest;
import dev.hieplp.library.payload.request.auth.register.ResendRegisterOtpRequest;
import dev.hieplp.library.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/request-to-register")
    public ResponseEntity<CommonResponse> requestToRegister(@Valid @RequestBody RequestToRegisterRequest request) {
        log.info("Request to register: {}", request);
        var data = authService.requestToRegister(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, data));
    }

    @PostMapping("/resend-register-otp")
    public ResponseEntity<CommonResponse> resendRegisterOtp(@Valid @RequestBody ResendRegisterOtpRequest request) {
        log.info("Resend register otp with request: {}", request);
        var data = authService.resendRegisterOtp(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, data));
    }

    @PostMapping("/confirm-register")
    public ResponseEntity<CommonResponse> confirmRegister(@Valid @RequestBody ConfirmRegisterRequest request) {
        log.info("Confirm register with request: {}", request);
        var data = authService.confirmRegister(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, data));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login with request: {}", request);
        var data = authService.login(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, data));
    }

    @PostMapping("/refresh-access-token")
    public ResponseEntity<CommonResponse> refreshAccessToken(@Valid @RequestBody RefreshAccessTokenRequest request) {
        log.info("Refresh access token with request: {}", request);
        var data = authService.refreshAccessToken(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, data));
    }
}
