package dev.hieplp.library.payload.request.auth.register;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResendRegisterOtpRequest {
    @NotNull(message = "OTP ID is required")
    private String otpId;
}
