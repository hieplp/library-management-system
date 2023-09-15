package dev.hieplp.library.service;

import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.GetListResponse;
import dev.hieplp.library.payload.request.user.CreateUserRequest;
import dev.hieplp.library.payload.request.user.UpdateUserRequest;
import dev.hieplp.library.payload.response.user.AdminUserResponse;

public interface AdminUserService {
    AdminUserResponse getUser(String userId);

    GetListResponse<AdminUserResponse> getUsers(GetListRequest request);

    AdminUserResponse createUser(CreateUserRequest request);

    AdminUserResponse updateUser(String userId, UpdateUserRequest request);
}
