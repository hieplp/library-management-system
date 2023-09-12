package dev.hieplp.library.service;

import dev.hieplp.library.payload.request.user.profile.UpdateOwnProfileRequest;
import dev.hieplp.library.payload.response.user.CommonUserResponse;

public interface UserService {
    CommonUserResponse getOwnProfile();

    CommonUserResponse updateOwnProfile(UpdateOwnProfileRequest request);
}
