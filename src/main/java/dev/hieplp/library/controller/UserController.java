package dev.hieplp.library.controller;

import dev.hieplp.library.common.enums.response.SuccessCode;
import dev.hieplp.library.common.payload.response.CommonResponse;
import dev.hieplp.library.payload.request.user.profile.UpdateOwnProfileRequest;
import dev.hieplp.library.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<CommonResponse> getOwnProfile() {
        log.info("Get own profile");
        var response = userService.getOwnProfile();
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @PatchMapping("/profile")
    public ResponseEntity<CommonResponse> updateOwnProfile(@Valid @RequestBody UpdateOwnProfileRequest request) {
        log.info("Update own profile");
        var response = userService.updateOwnProfile(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }
}
