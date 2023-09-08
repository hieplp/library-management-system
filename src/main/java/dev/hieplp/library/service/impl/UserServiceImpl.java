package dev.hieplp.library.service.impl;

import dev.hieplp.library.common.helper.UserHelper;
import dev.hieplp.library.common.util.DateTimeUtil;
import dev.hieplp.library.config.security.CurrentUser;
import dev.hieplp.library.payload.request.user.CommonUserResponse;
import dev.hieplp.library.payload.request.user.profile.UpdateOwnProfileRequest;
import dev.hieplp.library.repository.UserRepository;
import dev.hieplp.library.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final CurrentUser currentUser;

    private final UserRepository userRepo;

    private final UserHelper userHelper;

    private final DateTimeUtil dateTimeUtil;

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
}
