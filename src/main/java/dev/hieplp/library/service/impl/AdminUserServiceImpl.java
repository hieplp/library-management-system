package dev.hieplp.library.service.impl;

import dev.hieplp.library.common.enums.user.UserStatus;
import dev.hieplp.library.common.helper.UserHelper;
import dev.hieplp.library.common.util.DateTimeUtil;
import dev.hieplp.library.config.security.CurrentUser;
import dev.hieplp.library.payload.request.user.UpdateUserRequest;
import dev.hieplp.library.payload.response.user.AdminUserResponse;
import dev.hieplp.library.repository.UserRepository;
import dev.hieplp.library.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final CurrentUser currentUser;

    private final UserHelper userHelper;

    private final UserRepository userRepo;

    private final DateTimeUtil dateTimeUtil;

    @Override
    public AdminUserResponse getUser(String userId) {
        log.info("Get user by admin with userId: {}", userId);
        var user = userHelper.getUser(userId);
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
