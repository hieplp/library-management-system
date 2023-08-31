package dev.hieplp.library.payload.response.auth.register;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ResendRegisterOtpResponse extends RequestToRegisterResponse {
    private Integer resendCount;
    private Integer resendQuota;
}
