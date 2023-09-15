package dev.hieplp.library.controller.admin;

import dev.hieplp.library.common.enums.response.SuccessCode;
import dev.hieplp.library.common.payload.response.CommonResponse;
import dev.hieplp.library.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/users/{userId}/roles")
@RequiredArgsConstructor
public class AdminUserRoleController {

    private final UserRoleService userRoleService;

    @GetMapping
    public ResponseEntity<CommonResponse> getAllRoles(@PathVariable String userId) {
        log.info("Get all roles of user with userId: {}", userId);
        var response = userRoleService.getAllRoles(userId);
        return ResponseEntity.ok(new CommonResponse(SuccessCode.SUCCESS, response));
    }
}
