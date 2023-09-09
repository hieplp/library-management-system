package dev.hieplp.library.payload.request.auth.register;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfirmRegisterRequest {
    @NotBlank(message = "token is required")
    private String token;
}
