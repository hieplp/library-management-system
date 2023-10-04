package dev.hieplp.library.payload.request.auth;

import lombok.Data;

@Data
public class UpdateRootPasswordRequest {
    private String token;
    private String password;
}
