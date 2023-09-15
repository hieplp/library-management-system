package dev.hieplp.library.service;

import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.GetListResponse;
import dev.hieplp.library.payload.request.user.CreateUserRequest;
import dev.hieplp.library.payload.request.user.UpdateUserRequest;
import dev.hieplp.library.payload.request.user.profile.UpdateOwnProfileRequest;
import dev.hieplp.library.payload.response.user.AdminUserResponse;
import dev.hieplp.library.payload.response.user.CommonUserResponse;

public interface UserService {
    CommonUserResponse getOwnProfile();

    CommonUserResponse updateOwnProfile(UpdateOwnProfileRequest request);

    AdminUserResponse getUserByAdmin(String userId);

    GetListResponse<AdminUserResponse> getUsersByAdmin(GetListRequest request);

    AdminUserResponse createUserByAdmin(CreateUserRequest request);

    AdminUserResponse updateUserByAdmin(String userId, UpdateUserRequest request);
}
