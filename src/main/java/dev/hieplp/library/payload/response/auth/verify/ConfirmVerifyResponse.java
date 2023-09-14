package dev.hieplp.library.payload.response.auth.verify;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmVerifyResponse {
    private String maskedEmail;
}
