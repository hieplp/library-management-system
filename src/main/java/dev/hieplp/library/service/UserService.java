package dev.hieplp.library.service;

import dev.hieplp.library.payload.request.user.CommonUserResponse;
import dev.hieplp.library.payload.request.user.profile.UpdateOwnProfileRequest;

public interface UserService {
    CommonUserResponse getOwnProfile();

    CommonUserResponse updateOwnProfile(UpdateOwnProfileRequest request);
}
