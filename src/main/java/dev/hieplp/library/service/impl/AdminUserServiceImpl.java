package dev.hieplp.library.service.impl;

import dev.hieplp.library.common.entity.Password;
import dev.hieplp.library.common.entity.User;
import dev.hieplp.library.common.entity.UserRole;
import dev.hieplp.library.common.entity.key.UserRoleKey;
import dev.hieplp.library.common.enums.IdLength;
import dev.hieplp.library.common.enums.user.UserStatus;
import dev.hieplp.library.common.helper.UserHelper;
import dev.hieplp.library.common.util.DateTimeUtil;
import dev.hieplp.library.common.util.GeneratorUtil;
import dev.hieplp.library.config.security.CurrentUser;
import dev.hieplp.library.payload.request.user.CreateUserRequest;
import dev.hieplp.library.payload.request.user.UpdateUserRequest;
import dev.hieplp.library.payload.response.user.AdminUserResponse;
import dev.hieplp.library.repository.PasswordRepository;
import dev.hieplp.library.repository.UserRepository;
import dev.hieplp.library.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final CurrentUser currentUser;

    private final UserHelper userHelper;

    private final UserRepository userRepo;
    private final PasswordRepository passwordRepo;

    private final DateTimeUtil dateTimeUtil;
    private final GeneratorUtil generatorUtil;

    @Override
    public AdminUserResponse getUser(String userId) {
        log.info("Get user by admin with userId: {}", userId);
        var user = userHelper.getUser(userId);
        var response = new AdminUserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    @Override
    @Transactional
    public AdminUserResponse createUser(CreateUserRequest request) {
        log.info("Create user by admin with request: {}", request);

        // Check duplicated username and email
        userHelper.validateUsername(request.getUsername());
        userHelper.validateEmail(request.getEmail());

        final var userId = generatorUtil.generateId(IdLength.USER_ID);

        // Init roles
        var roles = new HashSet<UserRole>();
        request.getRoles().forEach(role -> roles.add(UserRole.builder()
                .id(UserRoleKey.builder()
                        .userId(userId)
                        .role(role)
                        .build())
                .build()));

        // Init user
        var user = User.builder()
                .userId(userId)
                .username(request.getUsername())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .avatar(request.getAvatar())
                .status(UserStatus.NOT_VERIFIED.getStatus())
                .roles(roles)
                .createdBy(currentUser.getUserId())
                .createdAt(dateTimeUtil.getCurrentTimestamp())
                .modifiedBy(currentUser.getUserId())
                .modifiedAt(dateTimeUtil.getCurrentTimestamp())
                .build();

        // Init password
        var password = Password.builder()
                .userId(userId)
                .password(null)
                .salt(null)
                .user(user)
                .build();

        passwordRepo.save(password);

        // Return response
        var response = new AdminUserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    @Override
    public AdminUserResponse updateUser(String userId, UpdateUserRequest request) {
        log.info("Update user: {} by admin with request: {}", userId, request);

        var user = userHelper.getUser(userId);
        var isChanged = false;

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
            isChanged = true;
        }

        if (request.getStatus() != null) {
            var status = UserStatus.fromStatus(request.getStatus());
            user.setStatus(status.getStatus());
            isChanged = true;
        }

        if (isChanged) {
            user
                    .setModifiedBy(currentUser.getUserId())
                    .setModifiedAt(dateTimeUtil.getCurrentTimestamp());
            userRepo.save(user);
        }

        var response = new AdminUserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }
}
