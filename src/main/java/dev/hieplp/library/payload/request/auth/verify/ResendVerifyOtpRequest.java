package dev.hieplp.library.payload.request.auth.verify;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResendVerifyOtpRequest {
    @NotBlank(message = "OTP ID is required")
    private String otpId;
}
