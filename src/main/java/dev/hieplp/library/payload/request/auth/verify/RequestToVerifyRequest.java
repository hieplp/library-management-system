package dev.hieplp.library.payload.request.auth.verify;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestToVerifyRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "OTP is required")
    private String email;
}
