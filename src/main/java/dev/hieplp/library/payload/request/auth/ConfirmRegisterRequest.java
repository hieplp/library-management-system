package dev.hieplp.library.payload.request.auth;

import lombok.Data;

@Data
public class ConfirmRegisterRequest {
    private String token;
}
