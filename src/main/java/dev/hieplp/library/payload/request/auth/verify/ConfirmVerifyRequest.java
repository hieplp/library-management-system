package dev.hieplp.library.payload.request.auth.verify;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfirmVerifyRequest {
    @NotBlank(message = "token is required")
    private String token;

    @NotBlank(message = "password is required")
    private String password;
}
