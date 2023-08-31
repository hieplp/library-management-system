package dev.hieplp.library.payload.request.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
