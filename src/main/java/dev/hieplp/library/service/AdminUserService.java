package dev.hieplp.library.service;

import dev.hieplp.library.payload.request.user.CreateUserRequest;
import dev.hieplp.library.payload.request.user.UpdateUserRequest;
import dev.hieplp.library.payload.response.user.AdminUserResponse;

public interface AdminUserService {
    AdminUserResponse getUser(String userId);

    AdminUserResponse createUser(CreateUserRequest request);

    AdminUserResponse updateUser(String userId, UpdateUserRequest request);
}
