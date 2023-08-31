package dev.hieplp.library.payload.request.auth.register;

import lombok.Data;

@Data
public class ConfirmRegisterRequest {
    private String token;
}
