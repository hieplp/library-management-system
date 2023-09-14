package dev.hieplp.library.payload.response.auth.verify;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Data
@SuperBuilder
public class RequestToVerifyResponse {
    private String otpId;
    private String maskedEmail;
    private Timestamp expiryTime;
    private Timestamp issuedTime;
}
