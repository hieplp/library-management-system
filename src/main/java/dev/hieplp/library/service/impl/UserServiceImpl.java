package dev.hieplp.library.service.impl;

import dev.hieplp.library.common.entity.Password;
import dev.hieplp.library.common.entity.User;
import dev.hieplp.library.common.entity.UserRole;
import dev.hieplp.library.common.entity.key.UserRoleKey;
import dev.hieplp.library.common.enums.IdLength;
import dev.hieplp.library.common.enums.user.Role;
import dev.hieplp.library.common.enums.user.UserStatus;
import dev.hieplp.library.common.helper.UserHelper;
import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.GetListResponse;
import dev.hieplp.library.common.util.DateTimeUtil;
import dev.hieplp.library.common.util.GeneratorUtil;
import dev.hieplp.library.common.util.SqlUtil;
import dev.hieplp.library.config.security.CurrentUser;
import dev.hieplp.library.payload.request.user.CreateUserRequest;
import dev.hieplp.library.payload.request.user.UpdateUserRequest;
import dev.hieplp.library.payload.request.user.profile.UpdateOwnProfileRequest;
import dev.hieplp.library.payload.response.user.AdminUserResponse;
import dev.hieplp.library.payload.response.user.CommonUserResponse;
import dev.hieplp.library.repository.PasswordRepository;
import dev.hieplp.library.repository.UserRepository;
import dev.hieplp.library.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final CurrentUser currentUser;

    private final UserRepository userRepo;
    private final PasswordRepository passwordRepo;

    private final UserHelper userHelper;

    private final DateTimeUtil dateTimeUtil;
    private final GeneratorUtil generatorUtil;
    private final SqlUtil sqlUtil;


    @Override
    public CommonUserResponse getOwnProfile() {
        log.info("Get own profile with userId: {}", currentUser.getUserId());
        var user = userHelper.getActiveUser(currentUser.getUserId());
        var response = new CommonUserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    @Override
    public CommonUserResponse updateOwnProfile(UpdateOwnProfileRequest request) {
        log.info("Update own profile with userId: {}", currentUser.getUserId());

        var user = userHelper.getActiveUser(currentUser.getUserId());

        // Update avatar
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        // Update full name
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        // Update
        user
                .setModifiedBy(currentUser.getUserId())
                .setModifiedAt(dateTimeUtil.getCurrentTimestamp());
        userRepo.save(user);

        var response = new CommonUserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }


    @Override
    public AdminUserResponse getUser(String userId) {
        log.info("Get user by admin with userId: {}", userId);
        var user = userHelper.getUser(userId);
        var response = new AdminUserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    @Override
    public GetListResponse<AdminUserResponse> getUsers(GetListRequest request) {
        log.info("Get users by admin with request: {}", request);
        var pages = sqlUtil.getPages(request, userRepo, AdminUserResponse.class);
        return GetListResponse.<AdminUserResponse>builder()
                .list(pages.getContent())
                .total(pages.getTotalElements())
                .build();
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
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            roles.add(UserRole.builder()
                    .id(UserRoleKey.builder()
                            .userId(userId)
                            .role(Role.USER.getRole())
                            .build())
                    .build());
        } else {
            request.getRoles().forEach(role -> {
                try {
                    var roleEnum = Role.fromRole(role);
                    roles.add(UserRole.builder()
                            .id(UserRoleKey.builder()
                                    .userId(userId)
                                    .role(roleEnum.getRole())
                                    .build())
                            .build());
                } catch (Exception e) {
                    // Ignore
                    log.warn("Unknown role: {}", role);
                }
            });
        }

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
