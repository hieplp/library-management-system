package dev.hieplp.library.controller.admin;

import dev.hieplp.library.common.enums.response.SuccessCode;
import dev.hieplp.library.common.payload.response.CommonResponse;
import dev.hieplp.library.payload.request.user.CreateUserRequest;
import dev.hieplp.library.payload.request.user.UpdateUserRequest;
import dev.hieplp.library.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AdminUserService userService;

    @PostMapping
    public ResponseEntity<CommonResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("Create user by admin with request: {}", request);
        var response = userService.createUser(request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponse> getUser(@PathVariable String userId) {
        log.info("Get user by admin with userId: {}", userId);
        var response = userService.getUser(userId);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<CommonResponse> updateUser(@PathVariable String userId,
                                                     @Valid @RequestBody UpdateUserRequest request) {
        log.info("Update user by admin with request: {}", request);
        var response = userService.updateUser(userId, request);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }
}
