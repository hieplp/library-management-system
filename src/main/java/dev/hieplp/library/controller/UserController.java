package dev.hieplp.library.controller;

import dev.hieplp.library.common.payload.response.CommonResponse;
import dev.hieplp.library.payload.request.user.RegisterUserRequest;
import dev.hieplp.library.service.UserService;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<CommonResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        log.info("Registering user with request: {}", request);
        return null;
    }

    @PostMapping("/confirm-register")
    public ResponseEntity<CommonResponse> confirmRegister() {
        log.info("Confirming register");
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login() {
        return null;
    }

}
