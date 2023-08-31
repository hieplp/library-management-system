package dev.hieplp.library.payload.response.auth.register;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmRegisterResponse {
    private String maskedEmail;
}
