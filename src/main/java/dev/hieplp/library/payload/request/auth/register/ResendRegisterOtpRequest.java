package dev.hieplp.library.payload.request.auth.register;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResendRegisterOtpRequest {
    @NotBlank(message = "OTP ID is required")
    private String otpId;
}
