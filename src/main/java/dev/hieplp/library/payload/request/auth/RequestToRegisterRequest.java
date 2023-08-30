package dev.hieplp.library.payload.request.auth;

import lombok.Data;

@Data
public class RequestToRegisterRequest {
    private String username;
    private String email;
}
