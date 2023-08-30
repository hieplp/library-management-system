package dev.hieplp.library.payload.request.user;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String username;
    private String email;
}
