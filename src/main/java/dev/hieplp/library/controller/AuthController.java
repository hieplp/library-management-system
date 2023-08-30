package dev.hieplp.library.controller;

import dev.hieplp.library.common.payload.response.CommonResponse;
import dev.hieplp.library.payload.request.auth.ConfirmRegisterRequest;
import dev.hieplp.library.payload.request.auth.RequestToRegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<CommonResponse> requestToRegister(@Valid @RequestBody RequestToRegisterRequest request) {
        log.info("Request to register: {}", request);
        return null;
    }

    @PutMapping("/register")
    public ResponseEntity<CommonResponse> confirmRegister(@Valid @RequestBody ConfirmRegisterRequest request) {
        log.info("Confirm register with request: {}", request);
        return null;
    }
}
